package com.yuushya.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SetHatItem extends AbstractYuushyaItem{
    public SetHatItem(Properties properties, Integer tipLines) {
        super(properties, tipLines);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        BlockPos blockPos = player.blockPosition();
        ItemStack handItemStack = player.getItemInHand(usedHand);
        if(usedHand == InteractionHand.MAIN_HAND){
            ItemStack offhandItemStack = player.getItemInHand(InteractionHand.OFF_HAND);
            if (!level.isClientSide) {
                if(!offhandItemStack.isEmpty()&&!handItemStack.isEmpty()){
                    if (player.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
                        player.setItemSlot(EquipmentSlot.HEAD, offhandItemStack.copy());
                        offhandItemStack.setCount(0);
                        handItemStack.setCount(handItemStack.getCount()-1);
                        return InteractionResultHolder.success(handItemStack);
                    } else {
                        return InteractionResultHolder.pass(handItemStack);
                    }
                }
            }
            return InteractionResultHolder.success(handItemStack);
        }
        return InteractionResultHolder.pass(handItemStack);
    }
}
