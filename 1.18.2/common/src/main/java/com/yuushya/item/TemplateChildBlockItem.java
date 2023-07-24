package com.yuushya.item;

import com.yuushya.Yuushya;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TemplateChildBlockItem extends BlockItem {

    private final String classType;
    private final String templateBlockName;
    private final String blockName;
    public TemplateChildBlockItem(Block block, Properties properties,String classType,String templateBlockName, String blockName) {
        super(block, properties);
        this.blockName = blockName;
        this.templateBlockName = templateBlockName;
        this.classType = classType;
    }
    @Override
    public Component getName(ItemStack stack) {
        String blockDescriptionId = Util.makeDescriptionId("block", classType.equals("remain")?new ResourceLocation(blockName):new ResourceLocation(Yuushya.MOD_ID,blockName));
        String templateBlockDescriptionId = Util.makeDescriptionId("item", new ResourceLocation(Yuushya.MOD_ID,templateBlockName));
        return new TranslatableComponent(templateBlockDescriptionId).append(" x ").append(new TranslatableComponent(blockDescriptionId));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {

    }
}
