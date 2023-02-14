package com.yuushya.items;

import com.yuushya.CreativeModeTabs;
import com.yuushya.mappings.Text;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class AbstractYuushyaItem extends ItemWithCreativeTabBase {
    private final Integer tipLines;

    /**
     * creativeModeTab: 创造物品栏
     * tipLines: 注释栏行数
     */
    public AbstractYuushyaItem(CreativeModeTabs.Wrapper creativeModeTab, Integer tipLines) {
        super(creativeModeTab, properties -> properties.stacksTo(1));
        this.tipLines = tipLines;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Level level, List<Component> tooltips, TooltipFlag tooltipFlag) {
        for (int i = 1; i <= tipLines; i++)
            tooltips.add(Text.translatable(this.getDescriptionId() + ".line" + i));
    }
}