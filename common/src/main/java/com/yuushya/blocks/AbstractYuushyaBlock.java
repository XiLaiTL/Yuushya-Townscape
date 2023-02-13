package com.yuushya.blocks;

import com.yuushya.mappings.Text;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;

import java.util.List;

public abstract class AbstractYuushyaBlock extends Block {
    private final Integer tipLines;

    public AbstractYuushyaBlock(Properties properties, Integer tipLines) {
        super(properties);
        this.tipLines = tipLines;

    }

    @Override
    public void appendHoverText(ItemStack itemStack, BlockGetter blockGetter, List<Component> tooltips, TooltipFlag tooltipFlag) {
        for (int i = 1; i <= tipLines; i++)
            tooltips.add(Text.translatable(this.getDescriptionId() + ".line" + i));
    }
}