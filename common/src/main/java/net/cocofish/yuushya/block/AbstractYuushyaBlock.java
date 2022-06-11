package net.cocofish.yuushya.block;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AbstractYuushyaBlock extends Block {

    private Integer tipLines;//注释栏数

    public AbstractYuushyaBlock(Properties properties,Integer tipLines) {
        super(properties);
        this.tipLines=tipLines;

    }

    @Override//注释栏数
    public void appendHoverText(ItemStack itemStack, @Nullable BlockGetter blockGetter, List<Component> tooltips, TooltipFlag tooltipFlag) {
        for(int i=1;i<=tipLines;i++)
            tooltips.add(new TranslatableComponent(this.getDescriptionId()+".line"+i));
    }
}
