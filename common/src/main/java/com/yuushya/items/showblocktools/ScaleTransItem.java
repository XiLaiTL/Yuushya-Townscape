package com.yuushya.items.showblocktools;

import com.yuushya.mappings.Text;
import com.yuushya.showblock.data.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ScaleTransItem extends PosTransItem {
    @Override
    public InteractionResult inMainHandRightClickOnBlock(Player player, BlockState blockState, Level level, BlockPos blockPos, ItemStack handItemStack) {
        return translateData(player, blockState, level, blockPos, handItemStack, (transformData) -> {
            Vector3f scales = transformData.scales;
            if (getForm() == 0) {
                scales.add(0.1f, 0.1f, 0.1f);
            }
            player.displayClientMessage(Text.translatable(this.getDescriptionId() + ".switch", scales.x(), scales.y(), scales.z()), true);
        });
    }

    @Override
    public InteractionResult inMainHandLeftClickOnBlock(Player player, BlockState blockState, Level level, BlockPos blockPos, ItemStack handItemStack) {
        return translateData(player, blockState, level, blockPos, handItemStack, (transformData) -> {
            Vector3f scales = transformData.scales;
            if (getForm() == 0) {
                scales.add(-0.1f, -0.1f, -0.1f);
            }
            player.displayClientMessage(Text.translatable(this.getDescriptionId() + ".switch", scales.x(), scales.y(), scales.z()), true);
        });
    }
}