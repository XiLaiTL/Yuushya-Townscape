package net.cocofish.yuushya.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public class AbstractToolItem extends AbstractYuushyaItem{
    public AbstractToolItem(Properties properties, Integer tipLines) {
        super(properties, tipLines);
    }
    @Override
    public boolean canAttackBlock(BlockState blockState, Level level, BlockPos blockPos, Player player) {
        inMainHandLeftClick(player, blockState, level, blockPos, player.getItemInHand(InteractionHand.MAIN_HAND));
        return false;
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        Player player = useOnContext.getPlayer();
        Level level = useOnContext.getLevel();
        InteractionHand hand= useOnContext.getHand();
        BlockPos blockPos = useOnContext.getClickedPos();

        if (hand == InteractionHand.OFF_HAND)
            return inOffHandRightClick(player, level.getBlockState(blockPos), level, blockPos, useOnContext.getItemInHand());
        else
            return inMainHandRightClick(player, level.getBlockState(blockPos), level, blockPos, useOnContext.getItemInHand());
    }

    public InteractionResult inMainHandRightClick(Player player, BlockState blockState, LevelAccessor level, BlockPos blockPos, ItemStack HandItemStack){
        return InteractionResult.PASS;
    }
    public InteractionResult inMainHandLeftClick(Player player, BlockState blockState, LevelAccessor level, BlockPos blockPos, ItemStack HandItemStack){
        return InteractionResult.PASS;
    }
    public InteractionResult inOffHandRightClick(Player player, BlockState blockState, LevelAccessor level, BlockPos blockPos, ItemStack HandItemStack){
        return InteractionResult.PASS;
    }
}

