package net.cocofish.yuushya.blockentity;

import net.minecraft.block.AirBlock;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.Inventory;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.collection.DefaultedList;

import java.util.List;
import java.util.function.Predicate;

public interface iBlockInventories {

    public static BlockState removeBlock(List<BlockState> blocks, int slot) {
        return slot >= 0 && slot < blocks.size() ? (BlockState) blocks.set(slot, Blocks.AIR.getDefaultState()) : Blocks.AIR.getDefaultState();

    }

    public static NbtCompound writeNbt(NbtCompound tag, DefaultedList<BlockState> block) {
        return writeNbt(tag, block, true);
    }

    public static NbtCompound writeNbt(NbtCompound tag, DefaultedList<BlockState> block, boolean setIfEmpty) {
        NbtList listTag = new NbtList();

        for(int i = 0; i < block.size(); ++i) {
            BlockState blockState = (BlockState)block.get(i);
            if (!(blockState.getBlock() instanceof AirBlock)) {
                NbtCompound compoundTag = new NbtCompound();
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

    public static void readNbt(NbtCompound tag, DefaultedList<BlockState> block) {
        NbtList listTag = tag.getList("Blocks", 10);

        for(int i = 0; i < listTag.size(); ++i) {
            NbtCompound compoundTag = listTag.getCompound(i);
            int j = compoundTag.getByte("Slot") & 255;
            if (j >= 0 && j < block.size()) {
                block.set(j, NbtHelper.toBlockState(compoundTag.getCompound("BlockState")));
            }
        }

    }


}
