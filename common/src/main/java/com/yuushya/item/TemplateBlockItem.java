package com.yuushya.item;


import com.yuushya.Yuushya;
import com.yuushya.block.YuushyaBlockFactory;
import com.yuushya.registries.YuushyaRegistries;
import com.yuushya.registries.YuushyaRegistryData;
import com.yuushya.utils.YuushyaLogger;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TemplateBlockItem extends AbstractYuushyaItem {
    public String templateType;
    private final YuushyaRegistryData.Block block;
    private final List<YuushyaRegistryData.Block> usageList;
    public TemplateBlockItem(Properties properties, Integer tipLines, String templateType) {
        super(properties, tipLines);
        this.templateType=templateType;//Registry.ITEM.getKey(this) 可以删掉的
        block = YuushyaRegistries.BlockTemplate.get(templateType);
        usageList=YuushyaRegistries.getTemplateUsageList(block);
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
                if (itemStackOffHand.getItem() instanceof BlockItem blockItem){
                    ResourceLocation resourceLocation = Registry.BLOCK.getKey(blockItem.getBlock());
                    if (resourceLocation.getNamespace().equals(Yuushya.MOD_ID)){
                        YuushyaRegistryData.Block blockFind=YuushyaRegistries.BlockALL.get(resourceLocation.getPath());
                        if (blockFind!=null&& usageList.contains(blockFind)){
                            BlockItem replaceBlockItem = (BlockItem)YuushyaRegistries.ITEMS.get(templateType+"_"+resourceLocation.getPath()).get();
                            return replaceBlockItem.useOn(useOnContext);
                        }
                    }
                    else {
                        YuushyaRegistryData.Block blockFind=YuushyaRegistries.BlockRemain.get(resourceLocation.toString());
                        if (blockFind!=null&& usageList.contains(blockFind)){
                            BlockItem replaceBlockItem = (BlockItem)YuushyaRegistries.ITEMS.get(templateType+"_"+resourceLocation.getPath()).get();
                            return replaceBlockItem.useOn(useOnContext);
                        }
                    }
                }
            }

        }
        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltips, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack,level,tooltips,tooltipFlag);
        TextComponent textComponent=new TextComponent("");int index=1;
        for (YuushyaRegistryData.Block block:usageList){
            ResourceLocation resourceLocation = block.classType.equals("remain")
                    ? new ResourceLocation(block.name)
                    : new ResourceLocation(Yuushya.MOD_ID,block.name);
            textComponent.append(new TranslatableComponent(Registry.BLOCK.get(resourceLocation).getDescriptionId())).append(" ");
            if (index%3==0||index==usageList.size()) {
                tooltips.add(textComponent);
                textComponent=new TextComponent("");
            }
        index++;}
    }
}
