package net.cocofish.yuushya.blockentity;

import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.collection.DefaultedList;

import javax.swing.text.html.HTMLDocument;

import java.util.List;

import static net.cocofish.yuushya.Yuushya.yuushyaElements;

public interface iBlockInventory {
    
        /**
         * Gets the block list of this inventory.
         * Must return the same instance every time it's called.
         */
        DefaultedList<BlockState> getBlocks();
        // Creation
        /**
         * Creates an inventory from the block list.
         */
        static iBlockInventory of(DefaultedList<BlockState> blocks) {
            return ()->blocks;
        }
        /**
         * Creates a new inventory with the size.
         */
        static iBlockInventory ofSize(int size) {

            return of(DefaultedList.ofSize(size, Blocks.AIR.getDefaultState()));
        }
        // Inventory
        /**
         * Returns the inventory size.
         */
        
        default int size() {
            return getBlocks().size();
        }
        /**
         * @return true if this inventory has only empty blocks, false otherwise
         */
        
        default boolean isEmpty() {
            for (int i = 0; i < size(); i++) {
                BlockState block = getBlock(i);
                if (block.getBlock() instanceof AirBlock) {
                    return false;
                }
            }
            return true;
        }
        /**
         * Gets the block in the slot.
         */
        
        default BlockState getBlock(int slot) {
            return getBlocks().get(slot);
        }
        /**
         * Takes a block of the size from the slot.
         * <p>(default implementation) If there are less blocks in the slot than what are requested,
         * takes all blocks in that slot.
         */

        /**
         * Removes the current block in the {@code slot} and returns it.
         */
        
        default BlockState removeBlock(List<BlockState> blocks, int slot) {
            return slot >= 0 && slot < blocks.size() ? (BlockState) blocks.set(slot, Blocks.AIR.getDefaultState()) : Blocks.AIR.getDefaultState();
        }
        default BlockState removeBlock( int slot) {
            return slot >= 0 && slot < getBlocks().size() ? (BlockState) getBlocks().set(slot, Blocks.AIR.getDefaultState()) : Blocks.AIR.getDefaultState();
        }


        default void setBlock(int slot, BlockState block) {
            getBlocks().set(slot, block);
        }
        /**
         * Clears {@linkplain #getBlocks() the block list}}.
         */
        
        default void clear() {
            getBlocks().clear();
        }
        
        default void markDirty() {
            // Override if you want behavior.
        }
        
        default boolean canPlayerUse(PlayerEntity player) {
            return true;
        }

}

