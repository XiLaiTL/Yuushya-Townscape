package com.yuushya.item;


import com.yuushya.Yuushya;
import com.yuushya.registries.YuushyaRegistries;
import com.yuushya.registries.YuushyaRegistryData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.StonecutterMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

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
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (level.isClientSide) {
            return InteractionResultHolder.success(player.getItemInHand(usedHand));
        }
        ItemStack itemStack = player.getItemInHand(usedHand).copy();
        if(player.isCreative()) player.setItemInHand(usedHand,ItemStack.EMPTY);
        else player.getItemInHand(usedHand).setCount(0);
        player.openMenu(getMenuProvider(level,player.blockPosition(), itemStack));
        player.awardStat(Stats.INTERACT_WITH_STONECUTTER);
        return InteractionResultHolder.consume(player.getItemInHand(usedHand));
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
                if (itemStackOffHand.getItem() instanceof BlockItem ){
                    BlockItem blockItem = (BlockItem)itemStackOffHand.getItem();
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
            else{
                if (level.isClientSide) {
                    return InteractionResult.SUCCESS;
                }
                ItemStack itemStack = player.getItemInHand(hand).copy();
                if(player.isCreative()) player.setItemInHand(hand,ItemStack.EMPTY);
                else player.getItemInHand(hand).setCount(0);
                player.openMenu(getMenuProvider(level,player.blockPosition(), itemStack));
                player.awardStat(Stats.INTERACT_WITH_STONECUTTER);//player.awardStat(Stats.ITEM_USED.get(this));
                return InteractionResult.CONSUME;
            }

        }
        return InteractionResult.PASS;
    }

    public static StonecutterMenu getStonecutterMenu(int i, Inventory inventory, Level level, BlockPos pos, ItemStack itemStack){
        return new StonecutterMenu(i, inventory, ContainerLevelAccess.create(level, pos)){
            {
                this.getSlot(0).set(itemStack);
            }

            @Override
            public boolean stillValid(Player _player) {
                return !container.getItem(0).isEmpty();
            }

        };
    }
    public MenuProvider getMenuProvider(Level level, BlockPos pos, ItemStack itemStack){
        return new SimpleMenuProvider((i, inventory, _player) -> getStonecutterMenu(i,inventory,level,pos,itemStack) , getDescription());
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
            if (index%6==0||index==usageList.size()) {
                tooltips.add(textComponent);
                textComponent=new TextComponent("");
            }
        index++;}
    }
}
