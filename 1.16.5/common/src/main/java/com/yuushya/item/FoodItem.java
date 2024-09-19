package com.yuushya.item;

import com.yuushya.registries.YuushyaRegistryData;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class FoodItem extends BlockItem {

    private ResourceLocation finishedItem = null;

    public FoodItem(Block block, YuushyaRegistryData.Block yuushyaBlock, Properties properties) {
        super(block, properties.food(foodProperties(yuushyaBlock)));
        if (yuushyaBlock.properties != null && yuushyaBlock.properties.food != null && yuushyaBlock.properties.food.finishedItem != null) {

            finishedItem = new  ResourceLocation( yuushyaBlock.properties.food.finishedItem);

        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player.isSecondaryUseActive()) {
            return super.useOn(context);
        } else {
            InteractionResultHolder<ItemStack> result = this.use(context.getLevel(), player, context.getHand());
            return result.getResult();
        }
    }

    public static FoodProperties foodProperties(YuushyaRegistryData.Block block) {
        int forms = 1;
        if (block.blockstate != null && block.blockstate.forms != null) {
            forms = block.blockstate.forms.size();
        }
        if (block.properties != null && block.properties.food != null) {
            YuushyaRegistryData.Item.Properties.Food food = block.properties.food;
            return new FoodProperties.Builder().nutrition(food.nutrition * forms).saturationMod(food.saturation * forms).build();
        }
        return Foods.APPLE;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (this.isEdible()) {
            ItemStack itemStack = player.getItemInHand(usedHand);
            if (player.canEat(this.getFoodProperties().canAlwaysEat())) {
                player.startUsingItem(usedHand);
//                if ( player instanceof ServerPlayer serverPlayer)
//                    if (this.finishedItem != null && BuiltInRegistries.ITEM.containsKey(this.finishedItem))
//                        giveItem(BuiltInRegistries.ITEM.get(this.finishedItem), serverPlayer, 1);
                return InteractionResultHolder.consume(itemStack);
            }
            return InteractionResultHolder.fail(itemStack);
        }
        return InteractionResultHolder.pass(player.getItemInHand(usedHand));
    }
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        ItemStack itemStack = super.finishUsingItem(stack, level, livingEntity);
        if (livingEntity instanceof Player && ((Player)livingEntity).abilities.instabuild) {
            return itemStack;
        }
        if (!level.isClientSide){
            if (livingEntity instanceof Player){
                Player player = (Player) livingEntity;
                if (this.finishedItem != null && Registry.ITEM.containsKey(this.finishedItem))
                    giveItem(Registry.ITEM.get(this.finishedItem), player, 1);

            }
        }

        return itemStack;
    }
    // 没有判断count超过最大值的情况
    private static void giveItem(Item item, Player player, int count) {
        int maxStackSize = item.getMaxStackSize();
        int currenCount = count;


                int onceGiveCount = Math.min(maxStackSize, currenCount);
                currenCount -= onceGiveCount;
                ItemStack itemStack = new ItemStack(item, count);
                boolean canGive = player.inventory.add(itemStack);
                ItemEntity itemEntity;
                if (canGive && itemStack.isEmpty()) {
                    itemStack.setCount(1);
                    itemEntity = player.drop(itemStack, false);
                    if (itemEntity != null) {
                        itemEntity.makeFakeItem();
                    }

                    player.level.playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    player.containerMenu.broadcastChanges();
                } else {
                    itemEntity = player.drop(itemStack, false);
                    if (itemEntity != null) {
                        itemEntity.setNoPickUpDelay();
                        itemEntity.setOwner(player.getUUID());
                    }
                }
            }
//    if (livingEntity instanceof Player && ((Player)livingEntity).getAbilities().instabuild) {
}
