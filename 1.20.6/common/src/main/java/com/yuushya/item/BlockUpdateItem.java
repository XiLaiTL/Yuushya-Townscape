package com.yuushya.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class BlockUpdateItem extends AbstractToolItem {
    public BlockUpdateItem(Properties properties, Integer tipLines) {
        super(properties, tipLines);
    }

    public static void updateClickBlock(Player player, BlockState blockState, Level level, BlockPos blockPos, ItemStack handItemStack){
        if(level.getBlockEntity(blockPos)!=null){
            return;
        }
        Direction direction = player.getDirection();
        if(blockState.hasProperty(BlockStateProperties.HORIZONTAL_FACING)){
            direction = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite();
        }
        else if (blockState.hasProperty(BlockStateProperties.FACING)){
            direction = blockState.getValue(BlockStateProperties.FACING);
            if(direction.getAxis() != Direction.Axis.Y){
                direction = direction.getOpposite();
            }
        }
        BlockState blockStateNew = blockState.getBlock().getStateForPlacement(new BlockPlaceContext(player, InteractionHand.MAIN_HAND,handItemStack,new BlockHitResult(new Vec3(player.getX(), player.getY(), player.getZ()),direction,blockPos,false)));
        level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 35);
        //level.levelEvent(player, 2001, blockPos, Block.getId(blockState));
        level.setBlockAndUpdate(blockPos,blockStateNew);
        level.gameEvent(player, GameEvent.BLOCK_CHANGE, blockPos);
    }

    //对方块主手右键
    @Override
    public InteractionResult inMainHandRightClickOnBlock(Player player, BlockState blockState, Level level, BlockPos blockPos, ItemStack handItemStack){
        updateClickBlock(player,blockState,level,blockPos,handItemStack);
        return InteractionResult.SUCCESS;
    }

    //对方块副手右键
    @Override
    public InteractionResult inOffHandRightClickOnBlock(Player player, BlockState blockState, Level level, BlockPos blockPos, ItemStack handItemStack){
        updateClickBlock(player,blockState,level,blockPos,handItemStack);
        return InteractionResult.SUCCESS;
    }
}
