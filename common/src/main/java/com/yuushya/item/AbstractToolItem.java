package com.yuushya.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class AbstractToolItem extends AbstractYuushyaItem{
    public AbstractToolItem(Properties properties, Integer tipLines) {
        super(properties, tipLines);
    }
    @Override
    public boolean canAttackBlock(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Player player) {
        inMainHandLeftClickOnBlock(player, blockState, level, blockPos, player.getItemInHand(InteractionHand.MAIN_HAND));
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        BlockPos blockPos = player.blockPosition();
        ItemStack handItemStack = player.getItemInHand(hand);
        if (hand == InteractionHand.OFF_HAND)
            return new  InteractionResultHolder<>(inOffHandRightClickInAir(player,level.getBlockState(blockPos),level,blockPos,handItemStack),handItemStack);
        else
            return new InteractionResultHolder<>(inMainHandRightClickInAir(player,level.getBlockState(blockPos),level,blockPos,handItemStack),handItemStack);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        Player player = useOnContext.getPlayer();
        Level level = useOnContext.getLevel();
        InteractionHand hand= useOnContext.getHand();
        BlockPos blockPos = useOnContext.getClickedPos();

        if (hand == InteractionHand.OFF_HAND)
            return inOffHandRightClickOnBlock(player, level.getBlockState(blockPos), level, blockPos, useOnContext.getItemInHand());
        else
            return inMainHandRightClickOnBlock(player, level.getBlockState(blockPos), level, blockPos, useOnContext.getItemInHand());
    }

    //对方块主手右键
    public InteractionResult inMainHandRightClickOnBlock(Player player, BlockState blockState, LevelAccessor level, BlockPos blockPos, ItemStack handItemStack){
        return InteractionResult.PASS;
    }
    //对方块主手左键
    public InteractionResult inMainHandLeftClickOnBlock(Player player, BlockState blockState, LevelAccessor level, BlockPos blockPos, ItemStack handItemStack){
        return InteractionResult.PASS;
    }
    //对方块副手右键
    public InteractionResult inOffHandRightClickOnBlock(Player player, BlockState blockState, LevelAccessor level, BlockPos blockPos, ItemStack handItemStack){
        return InteractionResult.PASS;
    }
    //对空气主手右键
    public InteractionResult inMainHandRightClickInAir(Player player, BlockState blockState, LevelAccessor level, BlockPos blockPos, ItemStack handItemStack){
        return InteractionResult.PASS;
    }
    //对空气副手右键
    public InteractionResult inOffHandRightClickInAir(Player player, BlockState blockState, LevelAccessor level, BlockPos blockPos, ItemStack handItemStack){
        return InteractionResult.PASS;
    }

}

