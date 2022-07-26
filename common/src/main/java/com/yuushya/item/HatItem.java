package com.yuushya.item;

import dev.architectury.extensions.ItemExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Wearable;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HatItem extends AbstractYuushyaItem implements Wearable,ItemExtension {
    public HatItem(Properties properties, Integer tipLines) {
        super(properties.stacksTo(1), tipLines);

    }
    @Override
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        BlockPos blockPos = player.blockPosition();
        ItemStack handItemStack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            if (player.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
                player.setItemSlot(EquipmentSlot.HEAD, handItemStack.copy());
                handItemStack.setCount(0);
                return InteractionResultHolder.success(handItemStack);
            } else {
                return InteractionResultHolder.pass(handItemStack);
            }
        }
        return InteractionResultHolder.success(handItemStack);
    }
    @Override
    @Nullable
    public EquipmentSlot getCustomEquipmentSlot(ItemStack stack) {
        return EquipmentSlot.HEAD;
    }
}
