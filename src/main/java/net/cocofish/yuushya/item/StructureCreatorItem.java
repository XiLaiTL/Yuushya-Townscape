package net.cocofish.yuushya.item;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.MessageType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Random;

public class StructureCreatorItem extends AbstractYuushyaItem {
    private Identifier createNbt;
    private Identifier cancelNbt;
    private BlockPos size;
    private BlockPos pos;
    private int mode=0;
    private int rot=0;
    private int mirror=0;
    private int offsetx=0;
    private int offsetz =0;

    public StructureCreatorItem(Settings settings, String registname, int linecount, String createnbt, String cancelnbt) {
        super(settings, registname, linecount);
        createNbt = createnbt.equals("") ? null : Identifier.tryParse(createnbt);
        cancelNbt = cancelnbt.equals("") ? null : Identifier.tryParse(cancelnbt);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        ItemStack itemStack=playerEntity.getStackInHand(hand);
        if (!world.isClient && playerEntity != null) {
            ServerWorld serverWorld = (ServerWorld) world;
            fromTag(itemStack);
            if (hand == Hand.OFF_HAND) {
                changeIntDirection();
                sendMessageSwitchState(playerEntity,true,itemStack);
            }else {
                switch (mode){
                    case 1: if(createNbt==null) return TypedActionResult.consume(playerEntity.getStackInHand(hand));
                            else if(cancelNbt==null) setAir(serverWorld, createNbt);
                            else loadStructure(serverWorld,cancelNbt,this.pos,itemStack);
                            break;
                    case 2: changeIntMirror();
                            sendMessageSwitchState(playerEntity,false,itemStack);
                            break;
                    case 3: changeIntRotation();
                            sendMessageSwitchState(playerEntity,false,itemStack);
                            break;
                    default:return TypedActionResult.pass(playerEntity.getStackInHand(hand));
                }
            }
            return TypedActionResult.success(playerEntity.getStackInHand(hand));
        }
        return TypedActionResult.pass(playerEntity.getStackInHand(hand));
    }
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity playerEntity=context.getPlayer();
        Hand hand= context.getHand();
        World world=context.getWorld();

        BlockPos pos=context.getBlockPos();
        BlockPos pos1=pos.offset(Direction.Axis.Y,1);
        ItemStack itemStack=playerEntity.getStackInHand(hand);
        if (!world.isClient && playerEntity != null) {
            fromTag(itemStack);
            ServerWorld serverWorld=(ServerWorld) world;
            if(hand==Hand.MAIN_HAND&&mode==0){
                if(createNbt==null) return ActionResult.CONSUME;
                this.pos=new BlockPos(pos1.getX(),pos1.getY(),pos1.getZ());
                loadStructure(serverWorld,createNbt,this.pos,itemStack);
                return ActionResult.SUCCESS;
            }

        }
        return ActionResult.PASS;
    }
    public boolean loadStructure(ServerWorld serverWorld,Identifier structureName,BlockPos blockPos,ItemStack itemStack) {
        if (structureName != null) {
            StructureManager structureManager = serverWorld.getStructureManager();
            BlockPos blockPos1=blockPos;
            Structure structure2;
            try {
                structure2 = structureManager.getStructure(structureName);
                BlockPos size=structure2.getSize();
                this.size=new BlockPos(size.getX(),size.getY(),size.getZ());
                toTag(itemStack);
                blockPos1=getOffset(pos);
            } catch (InvalidIdentifierException var6) {
                return false;
            }

            return structure2 == null ? false : this.place(serverWorld, structure2,blockPos1);
        } else {
            return false;
        }
    }
/*
    public void unloadStructure(ServerWorld serverWorld,Identifier structureName) {
        if (structureName != null) {
            StructureManager structureManager = serverWorld.getStructureManager();
            structureManager.unloadStructure(structureName);
        }
    }
*/
    public boolean place(ServerWorld serverWorld, Structure structure,BlockPos blockPos) {
            StructurePlacementData structurePlacementData = new StructurePlacementData().setMirror(BlockMirror.values()[this.mirror]).setRotation(BlockRotation.values()[this.rot]);
            structure.place(serverWorld, blockPos, structurePlacementData, new Random(Util.getMeasuringTimeMs()));
            return true;

    }
    public boolean setAir(ServerWorld serverWorld, Identifier structureName){
        if(pos==null||size==null) return false;
        BlockPos blockPos=getOffset(pos);
        for(int i = 0; Math.abs(i)<=size.getX();i=(offsetx<0?i-1:i+1))
            for(int j=0;Math.abs(j)<=size.getZ();j=(offsetz<0?j-1:j+1))
                for(int k=0;k<size.getY();k++)
                {
                    BlockPos blockPos1=new BlockPos(i+blockPos.getX(),k+blockPos.getY(),j+blockPos.getZ());
                    serverWorld.setBlockState(blockPos1, Blocks.AIR.getDefaultState(),3);
                    // serverWorld.syncWorldEvent(player, 2001, blockPos2, Block.getRawIdFromState(block2));

                }
        pos=null;
        return true;


    }
    private int changeIntDirection(){
        mode++;
        if(mode>3)mode=0;
        return mode;
    }
    private int changeIntRotation(){
        rot++;
        if(rot>=4)rot=0;
        return rot;
    }
    private int changeIntMirror(){
        mirror++;
        if(mirror>2)mirror=0;
        return mirror;
    }
    @Override
    public void fromTag(ItemStack itemStack){
        CompoundTag compoundTag=itemStack.getOrCreateTag();
        ListTag listTag = compoundTag.getList("Pos", 6);
        ListTag listTag1=compoundTag.getList("Size",6);
        pos=new BlockPos(listTag.getDouble(0),listTag.getDouble(1),listTag.getDouble(2));
        size=new BlockPos(listTag1.getDouble(0),listTag1.getDouble(1),listTag1.getDouble(2));
        mode=compoundTag.getInt("TransDirection");
        rot=compoundTag.getInt("rotation");
        mirror=compoundTag.getInt("mirror");
    }
    @Override
    public void toTag(ItemStack itemStack){
       CompoundTag compoundTag=itemStack.getOrCreateTag();
       compoundTag.put("Pos",toListTag(pos.getX(), pos.getY(), pos.getZ()));
       compoundTag.put("Size",toListTag(size.getX(), size.getY(), size.getZ()));
        compoundTag.putInt("TransDirection",mode);
        compoundTag.putInt("rotation",rot);
        compoundTag.putInt("mirror",mirror);

    }
    protected ListTag toListTag(double... values) {
        ListTag listTag = new ListTag();
        double[] var3 = values;
        int var4 = values.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            double d = var3[var5];
            listTag.add(DoubleTag.of(d));
        }

        return listTag;
    }
    private void sendMessageSwitchState(PlayerEntity player,boolean ismode,ItemStack itemStack){
        toTag(itemStack);
        if(ismode) sendMessage(player, new TranslatableText("item.yuushya.structure_creator" + "."+mode));
        else sendMessage(player, new TranslatableText( "item.yuushya.structure_creator.show",new Object[]{BlockMirror.values()[mirror].toString(),BlockRotation.values()[rot].toString()}));
    }
    private static void sendMessage(PlayerEntity player, Text message) {
        ((ServerPlayerEntity)player).sendMessage(message, MessageType.GAME_INFO, Util.NIL_UUID);
    }
    private BlockPos getOffset(BlockPos blockPos){
        switch (mirror){
            case 0:
                switch (rot){
                    case 0: offsetx=1;offsetz=1;break;
                    case 1: offsetx=-1;offsetz=1;break;
                    case 2: offsetx=-1;offsetz=-1;break;
                    case 3: offsetx=1;offsetz=-1;break;
                }
                break;
            case 1:
                switch (rot){
                    case 0: offsetx=1;offsetz=-1;break;
                    case 1: offsetx=1;offsetz=1;break;
                    case 2: offsetx=-1;offsetz=1;break;
                    case 3: offsetx=-1;offsetz=-1;break;
                }
                break;
            case 2:
                switch (rot){
                    case 0: offsetx=-1;offsetz=1;break;
                    case 1: offsetx=-1;offsetz=-1;break;
                    case 2: offsetx=1;offsetz=-1;break;
                    case 3: offsetx=1;offsetz=1;break;
                }
                break;
        }
        blockPos=blockPos.offset(Direction.Axis.X,-offsetx*size.getX()/2);
        blockPos=blockPos.offset(Direction.Axis.Z,-offsetz*size.getZ()/2);
        return blockPos;
    }

}
