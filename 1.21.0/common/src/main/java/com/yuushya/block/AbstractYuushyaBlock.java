package com.yuushya.block;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AbstractYuushyaBlock extends Block {

    private final Integer tipLines;//注释栏数

    public AbstractYuushyaBlock(Properties properties,Integer tipLines) {
        super(properties);
        this.tipLines=tipLines;

    }

    @Override//注释栏数
    public void appendHoverText(@NotNull ItemStack itemStack, Item.TooltipContext context, @NotNull List<Component> tooltips, @NotNull TooltipFlag tooltipFlag) {
        for(int i=1;i<=tipLines;i++)
            tooltips.add(Component.translatable(this.getDescriptionId()+".line"+i));
    }


}
