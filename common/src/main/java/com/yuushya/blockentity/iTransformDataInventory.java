package com.yuushya.blockentity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface iTransformDataInventory {
    //作为lambda类型的唯一抽象方法 the unique abstract function of interface and provide the lambda type.
    List<TransformData> getTransformDatas();
    //利用函数式构造参数类型的包装器，保证返回的实例与原来相同 Creates an inventory from the block list.Must return the same instance every time it's called.
    static iTransformDataInventory of(List<TransformData> transformDatas){return ()->transformDatas;}//interface ::=functional

    //default function for control the inventory
    default int size(){return getTransformDatas().size();}
    default boolean isEmpty(){
        for(TransformData transformData:getTransformDatas()){
            if (!(transformData.blockState.getBlock() instanceof AirBlock)) return false;
        }
        return true;
    }
    @NotNull
    default TransformData getTransformData(int slot){
        if (slot<size())
            return getTransformDatas().get(slot);
        else
            return getTransformDatas().get(Math.min(size() - 1, 0));
    }
    default void addTransformData(int slot,TransformData transformData){
        if (slot<size())
            getTransformDatas().add(slot,transformData);
        else
            getTransformDatas().add(transformData);
    }
    default void addTransformData(TransformData transformData){getTransformDatas().add(transformData);}
    default void removeTransformData(int slot){getTransformData(slot).set();}
    default void removeSlotBlockState(int slot){getTransformData(slot).blockState=Blocks.AIR.defaultBlockState();}
    default void setTransformData(int slot,TransformData transformData){getTransformData(slot).set(transformData);}
    default void setSlotBlockState(int slot,BlockState blockState){getTransformData(slot).blockState=blockState;}
    default void setSlotShown(int slot,boolean isShown){getTransformData(slot).isShown=isShown;}
    default void clear(){getTransformDatas().clear();}




    //readNbt from compoundTag
    static void load(CompoundTag compoundTag, List<TransformData> transformDatas) {
        ListTag listTag = compoundTag.getList("Blocks", 10);int index=0;//10 means Compound
        for(TransformData transformData:transformDatas){
            CompoundTag compoundTagTemp = listTag.getCompound(index);
            transformData.load(compoundTagTemp);
            //int slot = compoundTagTemp.getByte("Slot") & 255;
            //if(slot< transformDatas.size())
                //transformDatas.set(slot, NbtUtils.readTransformData(compoundTagTemp.getCompound("TransformData")));
        index++;}
    }

    //writeNbt to compoundTag
    static void saveAdditional(CompoundTag compoundTag, List<TransformData> transformDatas) {
        ListTag listTag=new ListTag(); int index=0;
        for(TransformData transformData:transformDatas){
            if(!(transformData.blockState.getBlock() instanceof AirBlock)){
                CompoundTag compoundTagTemp=new CompoundTag();
                compoundTagTemp.putByte("Slot",(byte)index);
                transformData.saveAdditional(compoundTagTemp);
                listTag.add(compoundTagTemp);
            }
        index++;}
        if(!listTag.isEmpty()) compoundTag.put("Blocks",listTag);
    }
}
