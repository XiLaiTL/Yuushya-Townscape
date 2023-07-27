package com.yuushya.item;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static com.yuushya.item.TemplateBlockItem.getStonecutterMenu;

public class BlueprintItem  extends AbstractYuushyaItem {

    public BlueprintItem(Properties properties, Integer tipLines) {
        super(properties, tipLines);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (level.isClientSide) {
            return InteractionResultHolder.success(player.getItemInHand(usedHand));
        }
        ItemStack itemStack = player.getItemInHand(usedHand).copy();
        if(player.isCreative()) player.setItemInHand(usedHand,ItemStack.EMPTY);
        else player.getItemInHand(usedHand).setCount(0);
        player.openMenu(getMenuProvider(level,player.blockPosition(), itemStack));
        player.awardStat(Stats.INTERACT_WITH_STONECUTTER);
        return InteractionResultHolder.consume(player.getItemInHand(usedHand));
    }

    public MenuProvider getMenuProvider(Level level, BlockPos pos, ItemStack itemStack){
        return new SimpleMenuProvider((i, inventory, _player) -> getStonecutterMenu(i,inventory,level,pos,itemStack) , new TranslatableComponent(this.getDescriptionId()));
    }
}
