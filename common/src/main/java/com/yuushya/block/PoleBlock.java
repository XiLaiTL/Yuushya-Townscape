package com.yuushya.block;

import com.yuushya.block.blockstate.PositionVerticalState;
import com.yuushya.block.blockstate.YuushyaBlockStates;
import com.yuushya.utils.YuushyaUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

import static com.yuushya.block.YuushyaBlockFactory.isTheSameBlock;
import static com.yuushya.block.YuushyaBlockFactory.isTheSameFacing;
import static com.yuushya.block.blockstate.YuushyaBlockStates.FORM;
import static com.yuushya.block.blockstate.YuushyaBlockStates.POS_VERTICAL;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class PoleBlock extends YuushyaBlockFactory.BlockWithClassType {
    public PoleBlock(Properties properties, Integer tipLines, String classType, boolean autoCollision) {
        super(properties, tipLines, classType,autoCollision);
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(HORIZONTAL_FACING, POS_VERTICAL,FORM);
    }
    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return blockPlaceContext.getClickedFace().getAxis() == Direction.Axis.Y
                ? this.defaultBlockState().setValue(HORIZONTAL_FACING, blockPlaceContext.getHorizontalDirection())
                : this.defaultBlockState().setValue(HORIZONTAL_FACING, blockPlaceContext.getClickedFace().getOpposite());
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        return getPositionOfPole(blockState,levelAccessor,blockPos);
    }

    public BlockState getPositionOfPole(BlockState stateIn,  LevelAccessor worldIn, BlockPos currentPos) {
        BlockState posUp = YuushyaUtils.getBlockState (worldIn.getBlockState(currentPos.above()),worldIn,currentPos.above());
        BlockState posDown =YuushyaUtils.getBlockState( worldIn.getBlockState(currentPos.below()),worldIn,currentPos.below());
        if (isTheSameBlock(posUp,stateIn) && isTheSameFacing(posUp,stateIn)){
            if(isTheSameBlock(posDown,stateIn) && isTheSameFacing(posDown,stateIn))
                return stateIn.setValue(POS_VERTICAL,PositionVerticalState.MIDDLE);
            else
                return stateIn.setValue(POS_VERTICAL,PositionVerticalState.BOTTOM);}
        else if(isTheSameBlock(posDown,stateIn) && isTheSameFacing(posDown,stateIn)){
            return stateIn.setValue(POS_VERTICAL,PositionVerticalState.TOP);}
        else {
            return stateIn.setValue(POS_VERTICAL,PositionVerticalState.NONE);}
    }
}
