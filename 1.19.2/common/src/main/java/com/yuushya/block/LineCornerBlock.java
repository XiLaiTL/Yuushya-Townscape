package com.yuushya.block;

import com.yuushya.block.blockstate.PositionHorizonState;
import com.yuushya.block.blockstate.ShapeState;
import com.yuushya.utils.YuushyaUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiPredicate;

import static com.yuushya.block.LineBlock.getPositionOfFace;
import static com.yuushya.block.YuushyaBlockFactory.isTheSameBlock;
import static com.yuushya.block.YuushyaBlockFactory.isTheSameFacing;
import static com.yuushya.block.blockstate.YuushyaBlockStates.POS_HORIZON;
import static com.yuushya.block.blockstate.YuushyaBlockStates.SHAPE;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class LineCornerBlock extends AbstractYuushyaBlockType {
    public LineCornerBlock() {
        super();
    }

    @Override
    public List<Property<?>> getBlockStateProperty() {
        return List.of(HORIZONTAL_FACING,POS_HORIZON,SHAPE);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        BlockState res= blockPlaceContext.getClickedFace().getAxis() == Direction.Axis.Y
                ? this.defaultBlockState().setValue(HORIZONTAL_FACING, blockPlaceContext.getHorizontalDirection())
                : this.defaultBlockState().setValue(HORIZONTAL_FACING, blockPlaceContext.getClickedFace().getOpposite());

        return res.setValue(POS_HORIZON,getPositionOfFaceWithCorner(res,blockPlaceContext.getLevel(),blockPlaceContext.getClickedPos(),LineCornerBlock::isConnected))
                .setValue(SHAPE,getLineShape(res,blockPlaceContext.getLevel(),blockPlaceContext.getClickedPos()))
                ;
    }
    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        BlockState res = blockState.setValue(POS_HORIZON,getPositionOfFaceWithCorner(blockState,levelAccessor,blockPos,LineCornerBlock::isConnected));
        return direction.getAxis().isHorizontal() ? res.setValue(SHAPE,getLineShape(res,levelAccessor,blockPos)) : res;
    }

    public static PositionHorizonState getPositionOfFaceWithCorner(BlockState state, LevelAccessor worldIn, BlockPos pos, BiPredicate<BlockState,BlockState> connected) {
        Direction facingDirection= state.getValue(HORIZONTAL_FACING);
        //处理的是在一条线上，遇到方块怎么办
        PositionHorizonState positionHorizonState = getPositionOfFace(state,worldIn,pos,connected);
        if(positionHorizonState!=PositionHorizonState.NONE) return positionHorizonState;
        //处理的是放置时垂直于已经形成的线，取后背的方块进行判断
        BlockState facingBlock = YuushyaUtils.getBlockState(worldIn.getBlockState(pos.relative(facingDirection)),worldIn,pos.relative(facingDirection));
        if(isTheSameBlock(state,facingBlock)){
            Direction direction2 = facingBlock.getValue(HORIZONTAL_FACING);
            if(direction2 == facingDirection.getClockWise()) return PositionHorizonState.LEFT;
            else if(direction2== facingDirection.getCounterClockWise()) return PositionHorizonState.RIGHT;
        }
        BlockState backBlock = YuushyaUtils.getBlockState(worldIn.getBlockState(pos.relative(facingDirection.getOpposite())),worldIn,pos.relative(facingDirection.getOpposite()));
        if(isTheSameBlock(state,backBlock)){
            Direction direction2 = backBlock.getValue(HORIZONTAL_FACING);
            if(direction2 == facingDirection.getClockWise()) return PositionHorizonState.RIGHT;
            else if(direction2== facingDirection.getCounterClockWise()) return PositionHorizonState.LEFT;
        }
        return PositionHorizonState.NONE;
    }

    public static ShapeState getLineShape(BlockState state, LevelAccessor level, BlockPos pos) {
        Direction facingDirection = state.getValue(HORIZONTAL_FACING);
        BlockState facingBlock = YuushyaUtils.getBlockState(level.getBlockState(pos.relative(facingDirection)),level,pos.relative(facingDirection));
        if (isTheSameBlock(state,facingBlock)) {
            Direction direction2 = facingBlock.getValue(HORIZONTAL_FACING);
            if (direction2.getAxis() != state.getValue(HORIZONTAL_FACING).getAxis() && canTakeShape(state, level, pos, direction2.getOpposite())) {
//                if (direction2 == facingDirection.getCounterClockWise()) {
//                    return StairsShape.OUTER_LEFT;
//                }
                return ShapeState.OUTER;
            }
        }

        BlockState backBlock = YuushyaUtils.getBlockState(level.getBlockState(pos.relative(facingDirection.getOpposite())),level,pos.relative(facingDirection.getOpposite()));
        if (isTheSameBlock(state,backBlock)) {
            Direction direction3 = backBlock.getValue(HORIZONTAL_FACING);
            if (direction3.getAxis() != state.getValue(HORIZONTAL_FACING).getAxis() && canTakeShape(state, level, pos, direction3)) {
//                if (direction3 == facingDirection.getCounterClockWise()) {
//                    return StairsShape.INNER_LEFT;
//                }
                return ShapeState.INNER;
            }
        }

        return ShapeState.STRAIGHT;
    }

    private static boolean canTakeShape(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
        BlockState blockState = level.getBlockState(pos.relative(face));
        return !isTheSameBlock(blockState,state) || blockState.getValue(HORIZONTAL_FACING) != state.getValue(HORIZONTAL_FACING) ;
    }

    public static boolean isConnected(BlockState state1,BlockState curState){
        return isTheSameFacing(state1,curState) ||
                (state1.getValue(SHAPE) != ShapeState.STRAIGHT && state1.getValue(HORIZONTAL_FACING).getOpposite()!=curState.getValue(HORIZONTAL_FACING));
    }
}