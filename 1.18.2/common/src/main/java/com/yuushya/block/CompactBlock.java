package com.yuushya.block;

import com.yuushya.registries.YuushyaRegistryData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.Nullable;

import static com.yuushya.block.FaceBlock.getPositionOfFaceX;
import static com.yuushya.block.FaceBlock.getPositionOfFaceZ;
import static com.yuushya.block.PoleBlock.getPositionOfPole;
import static com.yuushya.block.blockstate.YuushyaBlockStates.*;

public class CompactBlock extends YuushyaBlockFactory.BlockWithClassType {
    public CompactBlock(BlockBehaviour.Properties properties, Integer tipLines, String classType, String autoCollision, YuushyaRegistryData.Block.Usage usage) {
        super(properties, tipLines, classType,autoCollision, usage);
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(XPOS,YPOS, ZPOS);
    }
    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        LevelAccessor levelAccessor=blockPlaceContext.getLevel();
        BlockPos blockPos=blockPlaceContext.getClickedPos();
        return this.defaultBlockState()
                .setValue(XPOS, getPositionOfFaceX(this.defaultBlockState(),levelAccessor,blockPos))
                .setValue(YPOS,getPositionOfPole(this.defaultBlockState(),levelAccessor,blockPos))
                .setValue(ZPOS, getPositionOfFaceZ(this.defaultBlockState(),levelAccessor,blockPos));
    }
    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos){
        return stateIn
                .setValue(XPOS, getPositionOfFaceX(stateIn, worldIn, currentPos))
                .setValue(YPOS,getPositionOfPole(stateIn,worldIn,currentPos))
                .setValue(ZPOS, getPositionOfFaceZ(stateIn, worldIn, currentPos));
    }
}
