package net.cocofish.yuushya.blockentity;

import net.cocofish.yuushya.Yuushya;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.collection.DefaultedList;

public class ShelfBlockEntity extends BlockEntity implements iNventory, BlockEntityClientSerializable {
    private final DefaultedList<ItemStack> items;
    public ShelfBlockEntity() {

        super(Yuushya.shelfblockentity);
        items = DefaultedList.ofSize(2, ItemStack.EMPTY);
    }


    private int number = 7;

    private CompoundTag saveInitialChunkData(CompoundTag tag) {
        super.toTag(tag);
        Inventories.toTag(tag,items,true);
        return tag;
    }
    @Override
    public CompoundTag toTag(CompoundTag tag) {
        this.saveInitialChunkData(tag);
        tag.putInt("number", number);
        return tag;
    }
    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        number = tag.getInt("number");
        items.clear();
        Inventories.fromTag(tag,items);
    }

    @Override
    public void fromClientTag(CompoundTag tag) {
        items.clear();
        Inventories.fromTag(tag,items);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag)
    {
        Inventories.toTag(tag, items);
        return tag;
    }


    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }
}