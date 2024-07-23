package com.yuushya.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.ATTACH_FACE;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class AttachmentBlock extends AbstractYuushyaBlockType {
    public AttachmentBlock() {
        super();
    }

    @Override
    public List<Property<?>> getBlockStateProperty() {
        List<Property<?>> properties = new java.util.ArrayList<>();
        properties.add(HORIZONTAL_FACING);
        properties.add(ATTACH_FACE);
        return properties;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        //from FaceAttachedHorizontalDirectionalBlock
        for (Direction direction : blockPlaceContext.getNearestLookingDirections()) {
            BlockState blockState = direction.getAxis() == Direction.Axis.Y
                    ? this.defaultBlockState()
                    .setValue(ATTACH_FACE, direction == Direction.UP ? AttachFace.CEILING : AttachFace.FLOOR)
                    .setValue(HORIZONTAL_FACING, blockPlaceContext.getHorizontalDirection())
                    : this.defaultBlockState()
                    .setValue(ATTACH_FACE, AttachFace.WALL)
                    .setValue(HORIZONTAL_FACING, direction.getOpposite());
            if (!blockState.canSurvive(blockPlaceContext.getLevel(), blockPlaceContext.getClickedPos())) continue;
            return blockState;
        }
        return null;
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        return FaceAttachedHorizontalDirectionalBlock.canAttach(levelReader, blockPos, getConnectedDirection(blockState).getOpposite());
    }

    public static Direction getConnectedDirection(BlockState blockState) {
        switch (blockState.getValue(ATTACH_FACE)) {
            case CEILING:
                return Direction.DOWN;
            case FLOOR:
                return Direction.UP;
            default:
                return blockState.getValue(HORIZONTAL_FACING);
        }
    }
}
