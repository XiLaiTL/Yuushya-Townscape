package net.cocofish.yuushya.blockentity;

import net.cocofish.yuushya.Yuushya;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public class ShelfBlockEntity extends BlockEntity implements iNventory {
    private final DefaultedList<ItemStack> items;
    public ShelfBlockEntity(BlockPos blockPos,BlockState blockState) {

        super(Yuushya.shelfblockentity,blockPos,blockState);
        items = DefaultedList.ofSize(2, ItemStack.EMPTY);
    }


    private int number = 7;

    private NbtCompound saveInitialChunkData(NbtCompound tag) {
        super.writeNbt(tag);
        Inventories.writeNbt(tag,items,true);
        return tag;
    }
    @Override
    public void writeNbt(NbtCompound tag) {
        this.saveInitialChunkData(tag);
        tag.putInt("number", number);
    }
    @Override
    public void readNbt( NbtCompound tag) {
        super.readNbt( tag);
        number = tag.getInt("number");
        items.clear();
        Inventories.readNbt(tag,items);
    }


    public void fromClientTag(NbtCompound tag) {
        items.clear();
        Inventories.readNbt(tag,items);
    }


    public NbtCompound toClientTag(NbtCompound tag)
    {
        Inventories.writeNbt(tag, items);
        return tag;
    }


    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }
}