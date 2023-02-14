package com.yuushya.items.showblocktools;

import com.yuushya.Blocks;
import com.yuushya.CreativeModeTabs;
import com.yuushya.items.AbstractMultiPurposeToolItem;
import com.yuushya.showblock.ShowBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class GetShowBlockEntityItem extends AbstractMultiPurposeToolItem {

    /**
     * creativeModeTab: 创造物品栏
     * tipLines: 注释栏行数
     */
    public GetShowBlockEntityItem() {
        super(CreativeModeTabs.TEST_TABS, 4);
    }

    private static final int
            GET_SHOW_BLOCK = 0,
            GET_MIXED_BLOCK = 1,
            GET_ITEM = 2,
            RESTORE = 3;

    @Override
    public InteractionResult inMainHandRightClickOnBlock(Player player, BlockState blockState, Level level, BlockPos blockPos, ItemStack handItemStack) {
        switch (getForm()) {
            case GET_SHOW_BLOCK:
            case GET_MIXED_BLOCK: {
                if (blockState.getBlock() instanceof ShowBlock) return InteractionResult.PASS;
                level.setBlock(blockPos, Blocks.SHOW_BLOCK.get().defaultBlockState(), 35);
                ShowBlock.ShowBlockEntity showBlockEntity = (ShowBlock.ShowBlockEntity) level.getBlockEntity(blockPos);
                showBlockEntity.setSlotBlockState(0, blockState);
                showBlockEntity.getTransformData(0).isShown = true;
                showBlockEntity.setSlot(getForm());
                showBlockEntity.saveChanged();
                return InteractionResult.SUCCESS;
            }
            case GET_ITEM: {
                if (!(blockState.getBlock() instanceof ShowBlock)) return InteractionResult.PASS;
                ShowBlock.ShowBlockEntity showBlockEntity = (ShowBlock.ShowBlockEntity) level.getBlockEntity(blockPos);
                player.getInventory().placeItemBackInInventory(showBlockEntity.getTransFormDataNow().blockState.getBlock().asItem().getDefaultInstance());
                return InteractionResult.SUCCESS;
            }
            case RESTORE: {
                if (!(blockState.getBlock() instanceof ShowBlock)) return InteractionResult.PASS;
                ShowBlock.ShowBlockEntity showBlockEntity = (ShowBlock.ShowBlockEntity) level.getBlockEntity(blockPos);
                level.setBlock(blockPos, showBlockEntity.getTransFormDataNow().blockState, 35);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
}