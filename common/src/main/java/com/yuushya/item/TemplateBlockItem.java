package com.yuushya.item;


import com.yuushya.Yuushya;
import com.yuushya.block.YuushyaBlockFactory;
import com.yuushya.registries.YuushyaRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class TemplateBlockItem extends AbstractYuushyaItem {
    public String templateType;
    public TemplateBlockItem(Properties properties, Integer tipLines, String templateType) {
        super(properties, tipLines);
        this.templateType=templateType;
    }
    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        Player player = useOnContext.getPlayer();
        Level level = useOnContext.getLevel();
        InteractionHand hand= useOnContext.getHand();
        BlockPos blockPos = useOnContext.getClickedPos();

        if (hand == InteractionHand.MAIN_HAND){
            ItemStack itemStackOffHand=player.getItemInHand(InteractionHand.OFF_HAND);
            if (!itemStackOffHand.isEmpty()){
                if (itemStackOffHand.getItem() instanceof BlockItem blockItem)
                    if (blockItem.getBlock() instanceof YuushyaBlockFactory.BlockWithClassType block)
                        if (block.classType.contains("block:")){
                            //String name = Registry.BLOCK.getKey(block).getPath();
                            String name=block.classType.replace("block:","");
                            BlockItem replaceBlockItem = (BlockItem)YuushyaRegistries.ITEMS.get(templateType+"_"+name).get();
                            return replaceBlockItem.useOn(useOnContext);


                        }
            }

        }
        return InteractionResult.PASS;
    }
}
