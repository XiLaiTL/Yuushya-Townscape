package com.yuushya.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.yuushya.block.LineBlock.getPositionOfFace;
import static com.yuushya.block.PoleBlock.getPositionOfPole;
import static com.yuushya.block.blockstate.YuushyaBlockStates.*;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class PoleLineBlock extends AbstractYuushyaBlockType {

    @Override
    public List<Property<?>> getBlockStateProperty() {
        List<Property<?>> properties = new java.util.ArrayList<>();
        properties.add(HORIZONTAL_FACING);
        properties.add(POS_HORIZON);
        properties.add(YPOS);
        return properties;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        BlockState res= blockPlaceContext.getClickedFace().getAxis() == Direction.Axis.Y
                ? this.defaultBlockState().setValue(HORIZONTAL_FACING, blockPlaceContext.getHorizontalDirection())
                : this.defaultBlockState().setValue(HORIZONTAL_FACING, blockPlaceContext.getClickedFace().getOpposite());

        return res.setValue(POS_HORIZON,getPositionOfFace(res,blockPlaceContext.getLevel(),blockPlaceContext.getClickedPos(),YuushyaBlockFactory::isTheSameFacing))
                .setValue(YPOS,getPositionOfPole(res,blockPlaceContext.getLevel(),blockPlaceContext.getClickedPos()))
                ;
    }
    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        return blockState
                .setValue(POS_HORIZON,getPositionOfFace(blockState,levelAccessor,blockPos,YuushyaBlockFactory::isTheSameFacing))
                .setValue(YPOS, getPositionOfPole(blockState,levelAccessor,blockPos));
    }

}
