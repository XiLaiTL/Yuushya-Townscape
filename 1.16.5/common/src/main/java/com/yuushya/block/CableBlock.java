package com.yuushya.block;


import com.yuushya.registries.YuushyaRegistryData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.Nullable;

import static com.yuushya.block.YuushyaBlockFactory.isTheSameLine;
import static com.yuushya.block.blockstate.YuushyaBlockStates.DISTANCE;
import static com.yuushya.block.blockstate.YuushyaBlockStates.ISEND;
import static com.yuushya.utils.YuushyaUtils.getBlockState;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class CableBlock extends YuushyaBlockFactory.BlockWithClassType {

    public CableBlock(Properties properties, Integer tipLines, String classType, String autoCollision, YuushyaRegistryData.Block.Usage usage) {
        super(properties, tipLines, classType, autoCollision, usage);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(HORIZONTAL_FACING,ISEND,DISTANCE);
    }

    public static BlockState updateDistanceFromPost(BlockState state, LevelAccessor world, BlockPos pos){
        Direction direction=Direction.NORTH;
        int fromNW=0; int fromSE=0;
        Direction facingDirection= state.getValue(HORIZONTAL_FACING);
        switch (facingDirection.getAxis()){
            case Z : {
                BlockState nblockstate=getBlockState(world.getBlockState(pos.north()),world,pos.north());
                BlockState sblockstate=getBlockState(world.getBlockState(pos.south()),world,pos.south());
                Direction ndirection=nblockstate.getBlock() instanceof CableBlock ?nblockstate.getValue(HORIZONTAL_FACING) :state.getValue(HORIZONTAL_FACING);
                Direction sdirection=sblockstate.getBlock() instanceof CableBlock ?sblockstate.getValue(HORIZONTAL_FACING):state.getValue(HORIZONTAL_FACING);
                fromNW = getDistanceFromPost(nblockstate, state);
                fromSE = getDistanceFromPost(sblockstate, state);
                if(isCablePostBlock(nblockstate)) direction=Direction.NORTH;
                else if(isCablePostBlock(sblockstate)) direction=Direction.SOUTH;
                else {direction = fromNW < fromSE ? ndirection : sdirection;}
            }break;
            case X : {
                BlockState wblockstate=getBlockState(world.getBlockState(pos.west()),world,pos.west());
                BlockState eblockstate=getBlockState(world.getBlockState(pos.east()),world,pos.east());
                Direction wdirection=wblockstate.getBlock() instanceof CableBlock ?wblockstate.getValue(HORIZONTAL_FACING):state.getValue(HORIZONTAL_FACING);
                Direction edirection=eblockstate.getBlock() instanceof CableBlock ?eblockstate.getValue(HORIZONTAL_FACING):state.getValue(HORIZONTAL_FACING);
                fromNW = getDistanceFromPost(wblockstate, state);
                fromSE = getDistanceFromPost(eblockstate, state);
                if(isCablePostBlock(wblockstate)) direction=Direction.WEST;
                else if(isCablePostBlock(eblockstate)) direction=Direction.EAST;
                else {direction = fromNW < fromSE ? wdirection : edirection;}
            }break;
        }
        int distance=Math.min(fromNW,fromSE);
        boolean isend=fromNW==fromSE||Math.abs(fromNW-fromSE)==1;
        return state.setValue(DISTANCE,distance).setValue(ISEND,isend).setValue(HORIZONTAL_FACING,direction);
    }
    public static boolean isCablePostBlock(BlockState blockState){
        return blockState.getBlock() instanceof YuushyaBlockFactory.BlockWithClassType && ((YuushyaBlockFactory.BlockWithClassType)blockState.getBlock()).classType.equals("CablePostBlock");
    }
    public static int getDistanceFromPost(BlockState blockState,BlockState thisState){
        if(isCablePostBlock(blockState))
            return 0;
        else if(blockState.getBlock() instanceof CableBlock)
            if(isTheSameLine(blockState,thisState))
                return Math.min(blockState.getValue(DISTANCE) + 1, 15);
        return 15;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        BlockState res= blockPlaceContext.getClickedFace().getAxis() == Direction.Axis.Y
                ? this.defaultBlockState().setValue(HORIZONTAL_FACING, blockPlaceContext.getHorizontalDirection())
                : this.defaultBlockState().setValue(HORIZONTAL_FACING, blockPlaceContext.getClickedFace().getOpposite());
        return updateDistanceFromPost(res,blockPlaceContext.getLevel(),blockPlaceContext.getClickedPos());
    }
    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos){
        return updateDistanceFromPost(stateIn,worldIn,currentPos);
    }
}
