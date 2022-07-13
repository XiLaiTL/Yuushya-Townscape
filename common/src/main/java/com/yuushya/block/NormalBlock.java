package com.yuushya.block;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

public class NormalBlock extends YuushyaBlockFactory.BlockWithClassType {
    public NormalBlock(Properties properties, Integer tipLines, String classType) {
        super(properties, tipLines, classType);
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(BlockStateProperties.HORIZONTAL_FACING,BlockStateProperties.ATTACH_FACE,BlockStateProperties.POWERED);
    }
    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        BlockState res=this.defaultBlockState();
        //from FaceAttachedHorizontalDirectionalBlock
        Direction direction = blockPlaceContext.getNearestLookingDirection();
        res= direction.getAxis() == Direction.Axis.Y
                ? res
                .setValue(BlockStateProperties.ATTACH_FACE, direction == Direction.UP ? AttachFace.CEILING : AttachFace.FLOOR)
                .setValue(BlockStateProperties.HORIZONTAL_FACING, blockPlaceContext.getHorizontalDirection())
                : res
                .setValue(BlockStateProperties.ATTACH_FACE, AttachFace.WALL)
                .setValue(BlockStateProperties.HORIZONTAL_FACING, direction.getOpposite());
        return res;
    }
}
