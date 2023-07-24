package com.yuushya.block;

import com.yuushya.block.blockstate.PositionDirectionState;
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
import static com.yuushya.block.blockstate.YuushyaBlockStates.*;

public class FaceBlock extends YuushyaBlockFactory.BlockWithClassType {
    public FaceBlock(Properties properties, Integer tipLines, String classType, String autoCollision, YuushyaRegistryData.Block.Usage usage) {
        super(properties, tipLines, classType,autoCollision, usage);
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(XPOS, ZPOS,FORM);
    }
    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        LevelAccessor levelAccessor=blockPlaceContext.getLevel();
        BlockPos blockPos=blockPlaceContext.getClickedPos();
        return this.defaultBlockState()
                .setValue(XPOS, getPositionOfFace(this.defaultBlockState(),levelAccessor,blockPos,'X'))
                .setValue(ZPOS, getPositionOfFace(this.defaultBlockState(),levelAccessor,blockPos,'Z'));
    }
    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos){
        return stateIn
                .setValue(XPOS, getPositionOfFace(stateIn, worldIn, currentPos,'X'))
                .setValue(ZPOS, getPositionOfFace(stateIn, worldIn, currentPos,'Z'));
    }

    public static PositionDirectionState getPositionOfFace(BlockState state, LevelAccessor worldIn, BlockPos pos, char XorZ) {
        return switch (XorZ) {
            case 'X' -> {
                BlockState wblockstate=YuushyaUtils.getBlockState(worldIn.getBlockState(pos.west()),worldIn,pos.west());
                BlockState eblockstate=YuushyaUtils.getBlockState(worldIn.getBlockState(pos.east()), worldIn,pos.east());
                if (isTheSameBlock(wblockstate, state)) {
                    if (isTheSameBlock(eblockstate, state))
                        yield  PositionDirectionState.MIDDLE;
                    else
                        yield PositionDirectionState.EAST;}
                else if (isTheSameBlock(eblockstate, state)) {
                    yield PositionDirectionState.WEST;}
                else {
                    yield PositionDirectionState.NONE;}
            }
            case 'Z' -> {
                BlockState nblockstate= YuushyaUtils.getBlockState(worldIn.getBlockState(pos.north()), worldIn,pos.north());
                BlockState sblockstate=YuushyaUtils.getBlockState(worldIn.getBlockState(pos.south()),worldIn,pos.south());
                if (isTheSameBlock(nblockstate, state)) {
                    if (isTheSameBlock(sblockstate, state))
                        yield PositionDirectionState.MIDDLE;
                    else
                        yield PositionDirectionState.SOUTH;}
                else if (isTheSameBlock(sblockstate, state)) {
                    yield PositionDirectionState.NORTH;}
                else {
                    yield PositionDirectionState.NONE;}
            }
            default -> PositionDirectionState.NONE;
        };
    }

}
