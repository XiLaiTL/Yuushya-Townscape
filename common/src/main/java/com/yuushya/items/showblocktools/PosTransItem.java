package com.yuushya.items.showblocktools;

import com.yuushya.CreativeModeTabs;
import com.yuushya.items.AbstractMultiPurposeToolItem;
import com.yuushya.mappings.Text;
import com.yuushya.showblock.ShowBlock;
import com.yuushya.showblock.data.TransformData;
import com.yuushya.showblock.data.math.Vector3d;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Consumer;

public class PosTransItem extends AbstractMultiPurposeToolItem {
    private static final int MAX_POS_1 = 17;

    public PosTransItem() {
        super(CreativeModeTabs.TEST_TABS, 4);
        MAX_FORMS = 3;
    }

    @Override
    public InteractionResult inMainHandRightClickOnBlock(Player player, BlockState blockState, Level level, BlockPos blockPos, ItemStack handItemStack) {
        return translateData(player, blockState, level, blockPos, handItemStack, (transformData) -> {
            Vector3d pos = transformData.pos;
            switch (getForm()) {
                case 0:
                    pos.x = (pos.x - 1) % MAX_POS_1;
                    break;
                case 1:
                    pos.y = (pos.y - 1) % MAX_POS_1;
                    break;
                case 2:
                    pos.z = (pos.z - 1) % MAX_POS_1;
                    break;
            }
            player.displayClientMessage(Text.translatable(this.getDescriptionId() + ".switch", pos.x, pos.y, pos.z), true);
        });
    }

    @Override
    public InteractionResult inMainHandLeftClickOnBlock(Player player, BlockState blockState, Level level, BlockPos blockPos, ItemStack handItemStack) {
        return translateData(player, blockState, level, blockPos, handItemStack, (transformData) -> {
            Vector3d pos = transformData.pos;
            switch (getForm()) {
                case 0:
                    pos.x = (pos.x + 1) % MAX_POS_1;
                    break;
                case 1:
                    pos.y = (pos.y + 1) % MAX_POS_1;
                    break;
                case 2:
                    pos.z = (pos.z + 1) % MAX_POS_1;
                    break;
            }
            player.displayClientMessage(Text.translatable(this.getDescriptionId() + ".switch", pos.x, pos.y, pos.z), true);
        });
    }

    protected InteractionResult translateData(Player player, BlockState blockState, Level level, BlockPos blockPos, ItemStack handItemStack, Consumer<TransformData> consumer) {
        if (blockState.getBlock() instanceof ShowBlock) {
            ShowBlock.ShowBlockEntity showBlockEntity = (ShowBlock.ShowBlockEntity) level.getBlockEntity(blockPos);
            TransformData transformData = showBlockEntity.getTransFormDataNow();
            consumer.accept(transformData);
            showBlockEntity.saveChanged();
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}