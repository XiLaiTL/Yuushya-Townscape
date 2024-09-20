package com.yuushya.block;

import com.yuushya.block.blockstate.YuushyaBlockStates;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

public class FoodBlock {

    public static InteractionResult use(ResourceLocation finishedItem, int nutrition, float saturation, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (level.isClientSide) {
            if (FoodBlock.eat(finishedItem, nutrition, saturation, level, pos, state, player, hand, hit).consumesAction()) {
                return InteractionResult.SUCCESS;
            }
            if (itemStack.isEmpty()) {
                return InteractionResult.CONSUME;
            }
        }
        return FoodBlock.eat(finishedItem, nutrition, saturation, level, pos, state, player, hand, hit);
    }

    protected static InteractionResult eat(ResourceLocation finishedItem, int nutrition, float saturation, LevelAccessor level, BlockPos pos, BlockState state, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!player.canEat(false)) {
            return InteractionResult.PASS;
        }
        player.awardStat(Stats.EAT_CAKE_SLICE);
        player.getFoodData().eat(nutrition, saturation);
        int i = 0;
        IntegerProperty form = null;
        if (state.hasProperty(YuushyaBlockStates.FORM2)) form = YuushyaBlockStates.FORM2;
        else if (state.hasProperty(YuushyaBlockStates.FORM3)) form = YuushyaBlockStates.FORM3;
        else if (state.hasProperty(YuushyaBlockStates.FORM4)) form = YuushyaBlockStates.FORM4;
        else if (state.hasProperty(YuushyaBlockStates.FORM5)) form = YuushyaBlockStates.FORM5;
        else if (state.hasProperty(YuushyaBlockStates.FORM6)) form = YuushyaBlockStates.FORM6;
        else if (state.hasProperty(YuushyaBlockStates.FORM7)) form = YuushyaBlockStates.FORM7;
        else if (state.hasProperty(YuushyaBlockStates.FORM8)) form = YuushyaBlockStates.FORM8;
        level.gameEvent(player, GameEvent.EAT, pos);
        if (form != null) {
            i = state.getValue(form);
            if (i < YuushyaBlockStates.getFormMax(form) - 1) {
                level.setBlock(pos, state.setValue(form, i + 1), 3);
            } else {
                lastEat(finishedItem, level, pos, state, player, hand, hitResult);
            }
        }
        else {
            lastEat(finishedItem, level, pos, state, player, hand, hitResult);
        }
        return InteractionResult.SUCCESS;
    }

    public static void lastEat(ResourceLocation finishedItem, LevelAccessor level, BlockPos pos, BlockState state, Player player, InteractionHand hand, BlockHitResult hitResult) {

        level.removeBlock(pos, false);
        level.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);
        if (finishedItem != null && BuiltInRegistries.ITEM.containsKey(finishedItem)) {
            Item item = BuiltInRegistries.ITEM.get(finishedItem);
            if (item instanceof BlockItem blockItem) {
                blockItem.place(new BlockPlaceContext(player, hand, blockItem.getDefaultInstance(), hitResult));
            }
        }
    }
}
