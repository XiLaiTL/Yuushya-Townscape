package com.yuushya.block;

import com.yuushya.block.blockstate.PositionDirectionXState;
import com.yuushya.block.blockstate.PositionDirectionZState;
import com.yuushya.registries.YuushyaRegistryData;
import com.yuushya.utils.YuushyaUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.yuushya.block.YuushyaBlockFactory.isTheSameBlock;
import static com.yuushya.block.blockstate.YuushyaBlockStates.*;

public class FaceBlock extends AbstractYuushyaBlockType {
    public FaceBlock() {
        super();
    }

    @Override
    public List<Property<?>> getBlockStateProperty() {
        return List.of(XPOS, ZPOS);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        LevelAccessor levelAccessor=blockPlaceContext.getLevel();
        BlockPos blockPos=blockPlaceContext.getClickedPos();
        return this.defaultBlockState()
                .setValue(XPOS, getPositionOfFaceX(this.defaultBlockState(),levelAccessor,blockPos))
                .setValue(ZPOS, getPositionOfFaceZ(this.defaultBlockState(),levelAccessor,blockPos));
    }
    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos){
        return stateIn
                .setValue(XPOS, getPositionOfFaceX(stateIn, worldIn, currentPos))
                .setValue(ZPOS, getPositionOfFaceZ(stateIn, worldIn, currentPos));
    }

    public static PositionDirectionXState getPositionOfFaceX(BlockState state, LevelAccessor worldIn, BlockPos pos) {
        BlockState wblockstate=YuushyaUtils.getBlockState(worldIn.getBlockState(pos.west()),worldIn,pos.west());
        BlockState eblockstate=YuushyaUtils.getBlockState(worldIn.getBlockState(pos.east()), worldIn,pos.east());
        if (isTheSameBlock(wblockstate, state)) {
            if (isTheSameBlock(eblockstate, state))
                return PositionDirectionXState.MIDDLE;
            else
                return PositionDirectionXState.EAST;}
        else if (isTheSameBlock(eblockstate, state)) {
            return PositionDirectionXState.WEST;}
        else {
            return PositionDirectionXState.NONE;}
    }

    public static PositionDirectionZState getPositionOfFaceZ(BlockState state, LevelAccessor worldIn, BlockPos pos) {
        BlockState nblockstate= YuushyaUtils.getBlockState(worldIn.getBlockState(pos.north()), worldIn,pos.north());
        BlockState sblockstate=YuushyaUtils.getBlockState(worldIn.getBlockState(pos.south()),worldIn,pos.south());
        if (isTheSameBlock(nblockstate, state)) {
            if (isTheSameBlock(sblockstate, state))
                return  PositionDirectionZState.MIDDLE;
            else
                return PositionDirectionZState.SOUTH;}
        else if (isTheSameBlock(sblockstate, state)) {
            return PositionDirectionZState.NORTH;}
        else {
            return PositionDirectionZState.NONE;}
    }
}
