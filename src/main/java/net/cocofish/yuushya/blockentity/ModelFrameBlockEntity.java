package net.cocofish.yuushya.blockentity;

import net.cocofish.yuushya.Yuushya;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.collection.DefaultedList;

public class ModelFrameBlockEntity extends BlockEntity implements iNventory, BlockEntityClientSerializable {
    private final DefaultedList<ItemStack> items;
    public ModelFrameBlockEntity() {

        super(Yuushya.modelframeblockentity);
        items = DefaultedList.ofSize(2, ItemStack.EMPTY);
    }


    private int number = 7;
    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        // Save the current value of the number to the tag
        Inventories.toTag(tag,items,true);

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