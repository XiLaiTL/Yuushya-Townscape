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

        return res.setValue(POS_HORIZON,getPositionOfFaceWithCorner(res,blockPlaceContext.getLevel(),blockPlaceContext.getClickedPos()))
                .setValue(SHAPE,getLineShape(res,blockPlaceContext.getLevel(),blockPlaceContext.getClickedPos()))
                ;
    }
    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        BlockState res = blockState.setValue(POS_HORIZON,getPositionOfFaceWithCorner(blockState,levelAccessor,blockPos));
        return direction.getAxis().isHorizontal() ? res.setValue(SHAPE,getLineShape(res,levelAccessor,blockPos)) : res;
    }

    public static PositionHorizonState getPositionOfFaceWithCorner(BlockState state, LevelAccessor worldIn, BlockPos pos) {
        Direction facingDirection= state.getValue(HORIZONTAL_FACING);
        switch (facingDirection.getAxis()){
            case X -> {
                BlockState nblockstate= YuushyaUtils.getBlockState(worldIn.getBlockState(pos.north()), worldIn,pos.north());
                BlockState sblockstate=YuushyaUtils.getBlockState(worldIn.getBlockState(pos.south()),worldIn,pos.south());
                boolean nConnected=isTheSameBlock(nblockstate,state)&&isConnected(nblockstate,state);
                boolean sConnected=isTheSameBlock(sblockstate,state)&&isConnected(sblockstate,state);
                if (nConnected&&sConnected) return PositionHorizonState.MIDDLE;
                switch (facingDirection){
                    case WEST -> {
                        if(nConnected) return PositionHorizonState.LEFT;
                        if(sConnected) return PositionHorizonState.RIGHT;
                    }
                    case EAST -> {
                        if (nConnected) return PositionHorizonState.RIGHT;
                        if (sConnected) return PositionHorizonState.LEFT;
                    }
                }
            }
            case Z -> {
                BlockState wblockstate=YuushyaUtils.getBlockState(worldIn.getBlockState(pos.west()),worldIn,pos.west());
                BlockState eblockstate=YuushyaUtils.getBlockState(worldIn.getBlockState(pos.east()), worldIn,pos.east());
                boolean wConnected=isTheSameBlock(wblockstate,state)&&isConnected(wblockstate,state);
                boolean eConnected=isTheSameBlock(eblockstate,state)&&isConnected(eblockstate,state);
                if (wConnected&&eConnected) return PositionHorizonState.MIDDLE;
                switch (facingDirection) {
                    case NORTH -> {
                        if (wConnected) return PositionHorizonState.RIGHT;
                        if (eConnected) return PositionHorizonState.LEFT;
                    }
                    case SOUTH -> {
                        if (wConnected) return PositionHorizonState.LEFT;
                        if (eConnected) return PositionHorizonState.RIGHT;
                    }
                }
            }
        }
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

    public static ShapeState getLineShape(BlockState state, BlockGetter level, BlockPos pos) {
        Direction direction = state.getValue(HORIZONTAL_FACING);
        BlockState blockState = level.getBlockState(pos.relative(direction));
        if (isTheSameBlock(state,blockState)) {
            Direction direction2 = blockState.getValue(HORIZONTAL_FACING);
            if (direction2.getAxis() != state.getValue(HORIZONTAL_FACING).getAxis() && canTakeShape(state, level, pos, direction2.getOpposite())) {
//                if (direction2 == direction.getCounterClockWise()) {
//                    return StairsShape.OUTER_LEFT;
//                }
                return ShapeState.OUTER;
            }
        }

        BlockState blockState2 = level.getBlockState(pos.relative(direction.getOpposite()));
        if (isTheSameBlock(state,blockState2)) {
            Direction direction3 = blockState2.getValue(HORIZONTAL_FACING);
            if (direction3.getAxis() != state.getValue(HORIZONTAL_FACING).getAxis() && canTakeShape(state, level, pos, direction3)) {
//                if (direction3 == direction.getCounterClockWise()) {
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