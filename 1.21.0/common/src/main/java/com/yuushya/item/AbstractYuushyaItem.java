package com.yuushya.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class AbstractYuushyaItem extends Item {

    private Integer tipLines;//注释栏数

    public AbstractYuushyaItem(Properties properties,Integer tipLines) {
        super(properties);
        this.tipLines=tipLines;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, List<Component> tooltips, TooltipFlag tooltipFlag) {
        for(int i=1;i<=tipLines;i++)
            tooltips.add(Component.translatable(this.getDescriptionId()+".line"+i));
    }

}
