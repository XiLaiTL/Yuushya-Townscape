package net.cocofish.yuushya.blockentity;

import com.mojang.math.Vector3d;
import com.mojang.math.Vector3f;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public interface iTransformDataInventory {
    //作为lambda类型的唯一抽象方法 the unique abstract function of interface and provide the lambda type.
    NonNullList<TransformData> getTransformDatas();
    //利用函数式构造参数类型的包装器，保证返回的实例与原来相同 Creates an inventory from the block list.Must return the same instance every time it's called.
    static iTransformDataInventory of(NonNullList<TransformData> transformDatas){return ()->transformDatas;}//interface ::=functional
    //创建同类型新的容器 Creates a new inventory with the size.
    static iTransformDataInventory withSize(int size){return of(NonNullList.withSize(size,new TransformData()));}//没什么用，不能给方块实体扩容。

    //default function for control the inventory
    default int size(){return getTransformDatas().size();}
    default boolean isEmpty(){
        for(TransformData transformData:getTransformDatas()){
            if (!(transformData.blockState.getBlock() instanceof AirBlock)) return false;
        }
        return true;
    }
    @NotNull
    default TransformData getTransformData(int slot){return getTransformDatas().get(slot);}
    default void removeTransformData(int slot){getTransformData(slot).set();}
    default void removeSlotBlockState(int slot){getTransformData(slot).blockState=Blocks.AIR.defaultBlockState();}
    //都是预先填充的列表的对象进行更改
    default void setTransformData(int slot,TransformData transformData){getTransformData(slot).set(transformData);}
    default void setSlotBlockState(int slot,BlockState blockState){getTransformData(slot).blockState=blockState;}
    default void setSlotShown(int slot,boolean isShown){getTransformData(slot).isShown=isShown;}
    default void clear(){getTransformDatas().clear();}




    //readNbt from compoundTag
    static void load(CompoundTag compoundTag, NonNullList<TransformData> transformDatas) {
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
    static void saveAdditional(CompoundTag compoundTag, NonNullList<TransformData> transformDatas) {
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
