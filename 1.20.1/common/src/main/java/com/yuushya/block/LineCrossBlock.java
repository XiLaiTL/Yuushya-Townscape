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
import static com.yuushya.block.YuushyaBlockFactory.*;
import static com.yuushya.block.blockstate.YuushyaBlockStates.*;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class LineCrossBlock  extends AbstractYuushyaBlockType {
    public LineCrossBlock(){
        super();
    }

    @Override
    public List<Property<?>> getBlockStateProperty() {
        return List.of(HORIZONTAL_FACING,POS_HORIZON,FRONT,BACK);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        BlockState res= blockPlaceContext.getClickedFace().getAxis() == Direction.Axis.Y
                ? this.defaultBlockState().setValue(HORIZONTAL_FACING, blockPlaceContext.getHorizontalDirection())
                : this.defaultBlockState().setValue(HORIZONTAL_FACING, blockPlaceContext.getClickedFace().getOpposite());

        return res.setValue(POS_HORIZON,getPositionOfFace(res,blockPlaceContext.getLevel(),blockPlaceContext.getClickedPos(),LineCrossBlock::isConnected))
                .setValue(FRONT,getFrontState(res,blockPlaceContext.getLevel(),blockPlaceContext.getClickedPos()))
                .setValue(BACK,getBackState(res,blockPlaceContext.getLevel(),blockPlaceContext.getClickedPos()))
                ;
    }
    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        BlockState res = blockState.setValue(POS_HORIZON,getPositionOfFace(blockState,levelAccessor,blockPos,LineCrossBlock::isConnected));
        return direction.getAxis().isHorizontal()
                ? res.setValue(FRONT,getFrontState(res,levelAccessor,blockPos))
                    .setValue(BACK,getBackState(res,levelAccessor,blockPos))
                : res;
    }


    public static PositionHorizonState getFrontState(BlockState state, LevelAccessor level, BlockPos pos){
        Direction facingDirection = state.getValue(HORIZONTAL_FACING);
        BlockState facingBlock = level.getBlockState(pos.relative(facingDirection));
        if(isTheSameBlock(state,facingBlock)){
            Direction direction = facingBlock.getValue(HORIZONTAL_FACING);
            if(facingDirection == direction.getClockWise()) return PositionHorizonState.LEFT;
            else if(facingDirection== direction.getCounterClockWise()) return PositionHorizonState.RIGHT;
        }
        return PositionHorizonState.NONE;
        //留一个MIDDLE给未来一个方块可以两个栅栏的场景
    }

    public static PositionHorizonState getBackState(BlockState state, LevelAccessor level, BlockPos pos){
        Direction facingDirection = state.getValue(HORIZONTAL_FACING);
        BlockState backBlock = YuushyaUtils.getBlockState(level.getBlockState(pos.relative(facingDirection.getOpposite())),level,pos.relative(facingDirection.getOpposite()));
        if(isTheSameBlock(state,backBlock)){
            Direction direction = backBlock.getValue(HORIZONTAL_FACING);
            if(facingDirection == direction.getClockWise()) return PositionHorizonState.LEFT;
            else if(facingDirection== direction.getCounterClockWise()) return PositionHorizonState.RIGHT;
        }
        return PositionHorizonState.NONE;
        //留一个MIDDLE给未来一个方块可以两个栅栏的场景
    }

    public static boolean isConnected(BlockState state1,BlockState curState){
        return isTheSameFacing(state1,curState) || isPerpendicular(state1,curState);
    }
}
