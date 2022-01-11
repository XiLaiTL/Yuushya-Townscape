package net.cocofish.yuushya.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;

import java.util.List;

public class AbstractYuushyaItem extends Item {
    public int lines;
    public AbstractYuushyaItem(Settings settings, int linecount) {
        super(settings);
        lines=linecount;
    }
    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {

        //tooltip.add(new TranslatableText("item.yuushya."+name+".line1"));
        for(int i=1;i<=lines;i++) {
            tooltip.add(new TranslatableText(this.getTranslationKey()+".line"+ i)); }
    }

    public void writeNbt(ItemStack itemStack){
        //java to nbt
    }
    public void readNbt(ItemStack itemStack){
        //nbt to java
    }
}
