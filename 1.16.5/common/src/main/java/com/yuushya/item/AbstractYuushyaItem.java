package com.yuushya.item;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AbstractYuushyaItem extends Item {

    private Integer tipLines;//注释栏数

    public AbstractYuushyaItem(Properties properties,Integer tipLines) {
        super(properties);
        this.tipLines=tipLines;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltips, TooltipFlag tooltipFlag) {
        for(int i=1;i<=tipLines;i++)
            tooltips.add(new TranslatableComponent(this.getDescriptionId()+".line"+i));
    }


}
