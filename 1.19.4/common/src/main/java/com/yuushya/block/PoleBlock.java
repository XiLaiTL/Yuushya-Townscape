package com.yuushya.block;

import com.yuushya.block.blockstate.PositionDirectionZState;
import com.yuushya.block.blockstate.PositionVerticalState;
import com.yuushya.registries.YuushyaRegistryData;
import com.yuushya.utils.YuushyaUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.Nullable;

import static com.yuushya.block.YuushyaBlockFactory.isTheSameBlock;
import static com.yuushya.block.YuushyaBlockFactory.isTheSameFacing;
import static com.yuushya.block.blockstate.YuushyaBlockStates.*;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class PoleBlock extends YuushyaBlockFactory.BlockWithClassType {
    public PoleBlock(Properties properties, Integer tipLines, String classType, String autoCollision, YuushyaRegistryData.Block.Usage usage) {
        super(properties, tipLines, classType,autoCollision, usage);
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
        return blockState.setValue(POS_VERTICAL, getPositionOfPole(blockState,levelAccessor,blockPos));
    }
    public static PositionVerticalState getPositionOfPole(BlockState stateIn, LevelAccessor worldIn, BlockPos currentPos) {
        BlockState posUp = YuushyaUtils.getBlockState (worldIn.getBlockState(currentPos.above()),worldIn,currentPos.above());
        BlockState posDown =YuushyaUtils.getBlockState( worldIn.getBlockState(currentPos.below()),worldIn,currentPos.below());
        if (isTheSameBlock(posUp,stateIn) && isTheSameFacing(posUp,stateIn)){
            if(isTheSameBlock(posDown,stateIn) && isTheSameFacing(posDown,stateIn))
                return PositionVerticalState.MIDDLE;
            else
                return PositionVerticalState.BOTTOM;}
        else if(isTheSameBlock(posDown,stateIn) && isTheSameFacing(posDown,stateIn)){
            return PositionVerticalState.TOP;}
        else {
            return PositionVerticalState.NONE;}
    }

}
