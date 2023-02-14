package com.yuushya.items.showblocktools;

import com.yuushya.mappings.Text;
import com.yuushya.showblock.data.math.Vector3d;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class MicroPosTransItem extends PosTransItem {
    @Override
    public InteractionResult inMainHandRightClickOnBlock(Player player, BlockState blockState, Level level, BlockPos blockPos, ItemStack handItemStack) {
        return translateData(player, blockState, level, blockPos, handItemStack, (transformData) -> {
            Vector3d pos = transformData.pos;
            switch (getForm()) {
                case 0:
                    pos.x -= 0.001;
                    break;
                case 1:
                    pos.y -= 0.001;
                    break;
                case 2:
                    pos.z -= 0.001;
                    break;
                case 3:
                    pos.set(Math.round(pos.x) % 17, Math.round(pos.y) % 17, Math.round(pos.z) % 17);
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
                    pos.x += 0.001;
                    break;
                case 1:
                    pos.y += 0.001;
                    break;
                case 2:
                    pos.z += 0.001;
                    break;
            }
            player.displayClientMessage(Text.translatable(this.getDescriptionId() + ".switch", pos.x, pos.y, pos.z), true);
        });
    }
}