package com.yuushya.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public interface ITransformDataInventory {
    String BLOCKS_KEY = "Blocks";
    String SLOT_KEY = "Slot";

    List<TransformData> getTransformDatas();

    static ITransformDataInventory of(List<TransformData> transformDatas) {
        return () -> transformDatas;
    }

    default int size() {
        return getTransformDatas().size();
    }

    default boolean isEmpty() {
        for (TransformData transformData : getTransformDatas()) {
            if (!(transformData.blockState.getBlock() instanceof AirBlock)) return false;
        }
        return true;
    }

    default TransformData getTransformData(int slot) {
        if (slot < size())
            return getTransformDatas().get(slot);
        else
            return getTransformDatas().get(Math.min(size() - 1, 0));
    }

    default void addTransformData(int slot, TransformData transformData) {
        if (slot < size())
            getTransformDatas().add(slot, transformData);
        else
            getTransformDatas().add(transformData);
    }

    default void addTransformData(TransformData transformData) {
        getTransformDatas().add(transformData);
    }

    default void removeTransformData(int slot) {
        getTransformData(slot).set();
    }

    default void removeSlotBlockState(int slot) {
        getTransformData(slot).blockState = Blocks.AIR.defaultBlockState();
    }

    default void setTransformData(int slot, TransformData transformData) {
        getTransformData(slot).set(transformData);
    }

    default void setSlotBlockState(int slot, BlockState blockState) {
        getTransformData(slot).blockState = blockState;
    }

    default void setSlotShown(int slot, boolean isShown) {
        getTransformData(slot).isShown = isShown;
    }

    default void clear() {
        getTransformDatas().clear();
    }

    static void load(CompoundTag compoundTag, List<TransformData> transformDatas) {
        ListTag listTag = compoundTag.getList(BLOCKS_KEY, 10);
        if (!transformDatas.isEmpty()) transformDatas.clear();
        for (int index = 0; index < compoundTag.size(); index++) {
            TransformData transformData = new TransformData();
            CompoundTag compoundTagTemp = listTag.getCompound(index);
            transformData.load(compoundTagTemp);
            transformDatas.add(transformData);
        }
        if (transformDatas.isEmpty()) transformDatas.add(new TransformData());
    }

    static void saveAdditional(CompoundTag compoundTag, List<TransformData> transformDatas) {
        ListTag listTag = new ListTag();
        int index = 0;
        for (TransformData transformData : transformDatas) {
            if (!(transformData.blockState.getBlock() instanceof AirBlock)) {
                CompoundTag compoundTagTemp = new CompoundTag();
                compoundTagTemp.putByte(SLOT_KEY, (byte) index);
                transformData.saveAdditional(compoundTagTemp);
                listTag.add(compoundTagTemp);
            }
            index++;
        }
        if (!listTag.isEmpty()) compoundTag.put(BLOCKS_KEY, listTag);
    }
}