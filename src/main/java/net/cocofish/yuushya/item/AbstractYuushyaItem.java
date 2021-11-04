package net.cocofish.yuushya.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;

import java.util.List;

public class AbstractYuushyaItem extends Item {
    public String name;
    public int lines=1;
    public AbstractYuushyaItem(Settings settings,String registname,int linecount) {
        super(settings);
        name=registname;
        lines=linecount;
    }
    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {

        //tooltip.add(new TranslatableText("item.yuushya."+name+".line1"));
        for(int i=1;i<=lines;i++) {
            tooltip.add(new TranslatableText(this.getTranslationKey()+".line"+ i)); }
    }

    public void toTag(ItemStack itemStack){
        //java to nbt
    }
    public void fromTag(ItemStack itemStack){
        //nbt to java
    }
}
