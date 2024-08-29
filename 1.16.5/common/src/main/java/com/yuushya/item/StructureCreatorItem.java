package com.yuushya.item;

import net.minecraft.ResourceLocationException;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.yuushya.utils.YuushyaUtils.toListTag;

public class StructureCreatorItem extends AbstractMultiPurposeToolItem{
    private final ResourceLocation createNbt;
    private final ResourceLocation cancelNbt;
    private final int tipLines;
    private Vec3i size;
    private BlockPos pos;
    private int _rot=0,_mirror=0;
    private void changeIntMirror(){if (++_mirror>2) _mirror=0;}
    private void changeIntRotation(){if (++_rot>3) _rot=0;}
    private int _offsetX=0,_offsetZ=0;

    public StructureCreatorItem(Properties properties, Integer tipLines, String createNbt,String cancelNbt) {
        super(properties, tipLines);
        this.tipLines = tipLines;
        MAX_FORMS=4;
        this.createNbt=createNbt!=null?ResourceLocation.tryParse(createNbt):null;
        this.cancelNbt=cancelNbt!=null?ResourceLocation.tryParse(cancelNbt):null;
    }
    //对方块主手右键
    public InteractionResult inMainHandRightClickOnBlock(Player player, BlockState blockState, Level level, BlockPos blockPos, ItemStack handItemStack){
        if (level.isClientSide||player==null) return InteractionResult.PASS;
        BlockPos pos1=blockPos.relative(Direction.Axis.Y,1);
        getTag(handItemStack);
        if (getForm()==0){
            if (createNbt==null) return InteractionResult.CONSUME;
            this.pos=new BlockPos(pos1);
            loadStructure((ServerLevel) level,createNbt,pos,handItemStack);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
    //对空气主手右键
    public InteractionResult inMainHandRightClickInAir(Player player, BlockState blockState, Level level, BlockPos blockPos, ItemStack handItemStack){
        if (level.isClientSide||player==null) return InteractionResult.PASS;
        switch (getForm()){
            case 1:{
                if (createNbt==null) return InteractionResult.PASS;
                else if (cancelNbt==null) setAir((ServerLevel) level,createNbt);
                else loadStructure((ServerLevel)level,cancelNbt,this.pos,handItemStack);
            }break;
            case 2:
            case 3:{
                if (getForm()==2)changeIntMirror();
                else changeIntRotation();
                setTag(handItemStack);
                player.displayClientMessage(new TranslatableComponent("item.yuushya.structure_creator.show",Mirror.values()[_mirror].toString(),Rotation.values()[_rot].toString()),true);
            }break;
            default : {return InteractionResult.PASS;}
        }
        return InteractionResult.SUCCESS;
    }
    @Override
    public InteractionResult inOffHandRightClickInAir(Player player, BlockState blockStateTarget, Level level, BlockPos blockPos, ItemStack handItemStack){
        //左手右键用于切换工具状态
        getTag(handItemStack);
        changeForm();
        setTag(handItemStack);
        player.displayClientMessage(new TranslatableComponent("item.yuushya.structure_creator."+getForm()),true);
        return InteractionResult.PASS;
    }
    public boolean loadStructure(ServerLevel serverLevel,ResourceLocation structureName,BlockPos blockPos,ItemStack itemStack) {
        if (structureName != null) {
            StructureManager structureManager = serverLevel.getStructureManager();
            BlockPos blockPos1;
            StructureTemplate structure2;
            try {
                structure2 = structureManager.get(structureName);
                Vec3i size= null;
                if (structure2 != null) {
                    size = (structure2).getSize();
                    this.size=new Vec3i(size.getX(),size.getY(),size.getZ());
                    setTag(itemStack);
                    blockPos1=getOffset(pos);
                    return this.place(serverLevel, structure2,blockPos1);
                }
                return false;
            } catch (ResourceLocationException var6) {
                return false;
            }
        } else { return false;}
    }

    public boolean place(ServerLevel serverLevel, StructureTemplate structure, BlockPos blockPos) {
        StructurePlaceSettings structurePlacementData = new StructurePlaceSettings().setMirror(Mirror.values()[this._mirror]).setRotation(Rotation.values()[this._rot]);
        structure.placeInWorld(serverLevel,blockPos, blockPos, structurePlacementData, new Random(Util.getMillis()),2);
        return true;

    }
    public boolean setAir(ServerLevel serverLevel, ResourceLocation structureName){
        if(pos==null||size==null) return false;
        BlockPos blockPos=getOffset(pos);
        for(int i = 0; Math.abs(i)<=size.getX();i=(_offsetX<0?i-1:i+1))
            for(int j=0;Math.abs(j)<=size.getZ();j=(_offsetZ<0?j-1:j+1))
                for(int k=0;k<size.getY();k++) {
                    BlockPos blockPos1=new BlockPos(i+blockPos.getX(),k+blockPos.getY(),j+blockPos.getZ());
                    serverLevel.setBlock(blockPos1, Blocks.AIR.defaultBlockState(),3);
                    // serverWorld.syncWorldEvent(player, 2001, blockPos2, Block.getRawIdFromState(block2));
                }
        pos=null;
        return true;
    }



    @Override
    public void getTag(ItemStack itemStack){//readNbt
        super.getTag(itemStack);
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        ListTag listTag = compoundTag.getList("Pos", 6);
        ListTag listTag1=compoundTag.getList("Size",6);
        pos=new BlockPos(listTag.getDouble(0),listTag.getDouble(1),listTag.getDouble(2));
        size=new Vec3i(listTag1.getDouble(0),listTag1.getDouble(1),listTag1.getDouble(2));
        _rot=compoundTag.getInt("rotation");
        _mirror=compoundTag.getInt("mirror");
    }
    @Override
    public void setTag(ItemStack itemStack){//writeNbt
        super.setTag(itemStack);
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        compoundTag.put("Pos",toListTag(pos.getX(), pos.getY(), pos.getZ()));
        compoundTag.put("Size",toListTag(size.getX(), size.getY(), size.getZ()));
        compoundTag.putInt("rotation",_rot);
        compoundTag.putInt("mirror",_mirror);
        itemStack.setTag(compoundTag);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltips, TooltipFlag tooltipFlag) {
        tooltips.add(new TranslatableComponent("item.yuushya.structure_creator.line1"));
        for(int i=2;i<=tipLines;i++)
            tooltips.add(new TranslatableComponent(this.getDescriptionId()+".line"+i));
    }

    private BlockPos getOffset(BlockPos blockPos){
        switch (_mirror){
            case 0:
                switch (_rot) {
                    case 0:
                        _offsetX = 1;
                        _offsetZ = 1;
                        break;
                    case 1:
                        _offsetX = -1;
                        _offsetZ = 1;
                        break;
                    case 2:
                        _offsetX = -1;
                        _offsetZ = -1;
                        break;
                    case 3:
                        _offsetX = 1;
                        _offsetZ = -1;
                        break;
                }
                break;
            case 1:
                switch (_rot) {
                    case 0:
                        _offsetX = 1;
                        _offsetZ = -1;
                        break;
                    case 1:
                        _offsetX = 1;
                        _offsetZ = 1;
                        break;
                    case 2:
                        _offsetX = -1;
                        _offsetZ = 1;
                        break;
                    case 3:
                        _offsetX = -1;
                        _offsetZ = -1;
                        break;
                }
                break;
            case 2:
                switch (_rot) {
                    case 0:
                        _offsetX = -1;
                        _offsetZ = 1;
                        break;
                    case 1:
                        _offsetX = -1;
                        _offsetZ = -1;
                        break;
                    case 2:
                        _offsetX = 1;
                        _offsetZ = -1;
                        break;
                    case 3:
                        _offsetX = 1;
                        _offsetZ = 1;
                        break;
                }
                break;
        }
        blockPos=blockPos.relative(Direction.Axis.X,-this._offsetX *size.getX()/2);
        blockPos=blockPos.relative(Direction.Axis.Z,-this._offsetZ *size.getZ()/2);
        return blockPos;
    }

}
