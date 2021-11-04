package net.cocofish.yuushya.blockentity;

import net.minecraft.block.AirBlock;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.Inventory;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.collection.DefaultedList;

import java.util.List;
import java.util.function.Predicate;

public interface iBlockInventories {

    public static BlockState removeBlock(List<BlockState> blocks, int slot) {
        return slot >= 0 && slot < blocks.size() ? (BlockState) blocks.set(slot, Blocks.AIR.getDefaultState()) : Blocks.AIR.getDefaultState();

    }

    public static CompoundTag toTag(CompoundTag tag, DefaultedList<BlockState> block) {
        return toTag(tag, block, true);
    }

    public static CompoundTag toTag(CompoundTag tag, DefaultedList<BlockState> block, boolean setIfEmpty) {
        ListTag listTag = new ListTag();

        for(int i = 0; i < block.size(); ++i) {
            BlockState blockState = (BlockState)block.get(i);
            if (!(blockState.getBlock() instanceof AirBlock)) {
                CompoundTag compoundTag = new CompoundTag();
                compoundTag.putByte("Slot", (byte)i);
                compoundTag.put("BlockState", NbtHelper.fromBlockState(blockState));
                listTag.add(compoundTag);
            }
        }

        if (!listTag.isEmpty() || setIfEmpty) {
            tag.put("Blocks", listTag);
        }

        return tag;
    }

    public static void fromTag(CompoundTag tag, DefaultedList<BlockState> block) {
        ListTag listTag = tag.getList("Blocks", 10);

        for(int i = 0; i < listTag.size(); ++i) {
            CompoundTag compoundTag = listTag.getCompound(i);
            int j = compoundTag.getByte("Slot") & 255;
            if (j >= 0 && j < block.size()) {
                block.set(j, NbtHelper.toBlockState(compoundTag.getCompound("BlockState")));
            }
        }

    }


}
