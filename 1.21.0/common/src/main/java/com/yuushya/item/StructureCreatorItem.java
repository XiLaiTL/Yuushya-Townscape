package com.yuushya.item;

import com.yuushya.item.data_component.Structure;
import com.yuushya.registries.YuushyaRegistries;
import net.minecraft.ResourceLocationException;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.Optional;
import java.util.Random;

import static com.yuushya.utils.YuushyaUtils.toListTag;

public class StructureCreatorItem extends AbstractMultiPurposeToolItem{
    private final ResourceLocation createNbt;
    private final ResourceLocation cancelNbt;
    private BlockPos size;
    private BlockPos pos;
    private int _rot=0,_mirror=0;
    private void changeIntMirror(){if (++_mirror>2) _mirror=0;}
    private void changeIntRotation(){if (++_rot>3) _rot=0;}
    private int _offsetX=0,_offsetZ=0;

    public StructureCreatorItem(Properties properties, Integer tipLines, String createNbt,String cancelNbt) {
        super(properties, tipLines);
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
            case 1->{
                if (createNbt==null) return InteractionResult.PASS;
                else if (cancelNbt==null) setAir((ServerLevel) level,createNbt);
                else loadStructure((ServerLevel)level,cancelNbt,this.pos,handItemStack);
            }
            case 2,3->{
                if (getForm()==2)changeIntMirror();
                else changeIntRotation();
                setTag(handItemStack);
                player.displayClientMessage(Component.translatable("item.yuushya.structure_creator.show",Mirror.values()[_mirror].toString(),Rotation.values()[_rot].toString()),true);
            }
            default -> {return InteractionResult.PASS;}
        }
        return InteractionResult.SUCCESS;
    }
    @Override
    public InteractionResult inOffHandRightClickInAir(Player player, BlockState blockStateTarget, Level level, BlockPos blockPos, ItemStack handItemStack){
        //左手右键用于切换工具状态
        getTag(handItemStack);
        changeForm();
        setTag(handItemStack);
        player.displayClientMessage(Component.translatable("item.yuushya.structure_creator."+getForm()),true);
        return InteractionResult.PASS;
    }
    public boolean loadStructure(ServerLevel serverLevel,ResourceLocation structureName,BlockPos blockPos,ItemStack itemStack) {
        if (structureName != null) {
            StructureTemplateManager structureManager = serverLevel.getStructureManager();
            BlockPos blockPos1;
            Optional<StructureTemplate> structure2;
            try {
                structure2 = structureManager.get(structureName);
                Vec3i size=(structure2.get()).getSize();
                this.size=new BlockPos(size.getX(),size.getY(),size.getZ());
                setTag(itemStack);
                blockPos1=getOffset(pos);
                return this.place(serverLevel, structure2.get(),blockPos1);
            } catch (ResourceLocationException var6) {
                return false;
            }
        } else { return false;}
    }

    public boolean place(ServerLevel serverLevel, StructureTemplate structure, BlockPos blockPos) {
        StructurePlaceSettings structurePlacementData = new StructurePlaceSettings().setMirror(Mirror.values()[this._mirror]).setRotation(Rotation.values()[this._rot]);
        structure.placeInWorld(serverLevel,blockPos, blockPos, structurePlacementData, serverLevel.getRandom(),2);
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
        Structure data = (Structure) itemStack.getOrDefault(YuushyaRegistries.STRUCTURE.get(), Structure.EMPTY);
        pos=data.pos();
        size=data.size();
        _rot=data.rotation();
        _mirror=data.mirror();
    }
    @Override
    public void setTag(ItemStack itemStack){//writeNbt
        super.setTag(itemStack);
        itemStack.set((DataComponentType<Structure>) YuushyaRegistries.STRUCTURE.get(),new Structure(
                pos,size,_rot,_mirror
        ));
    }



    private BlockPos getOffset(BlockPos blockPos){
        switch (_mirror){
            case 0:
                switch (_rot) {
                    case 0 -> {_offsetX = 1;_offsetZ = 1;}
                    case 1 -> {_offsetX = -1;_offsetZ = 1;}
                    case 2 -> {_offsetX = -1;_offsetZ = -1;}
                    case 3 -> {_offsetX = 1;_offsetZ = -1;}
                }
                break;
            case 1:
                switch (_rot) {
                    case 0 -> {_offsetX = 1;_offsetZ = -1;}
                    case 1 -> {_offsetX = 1;_offsetZ = 1;}
                    case 2 -> {_offsetX = -1;_offsetZ = 1;}
                    case 3 -> {_offsetX = -1;_offsetZ = -1;}
                }
                break;
            case 2:
                switch (_rot) {
                    case 0 -> {_offsetX = -1;_offsetZ = 1;}
                    case 1 -> {_offsetX = -1;_offsetZ = -1;}
                    case 2 -> {_offsetX = 1;_offsetZ = -1;}
                    case 3 -> {_offsetX = 1;_offsetZ = 1;}
                }
                break;
        }
        blockPos=blockPos.relative(Direction.Axis.X,-this._offsetX *size.getX()/2);
        blockPos=blockPos.relative(Direction.Axis.Z,-this._offsetZ *size.getZ()/2);
        return blockPos;
    }

}
