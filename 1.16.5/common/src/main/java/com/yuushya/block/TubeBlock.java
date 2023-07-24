package com.yuushya.block;

import com.yuushya.registries.YuushyaRegistryData;
import com.yuushya.utils.YuushyaUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.Nullable;

import static com.yuushya.block.blockstate.YuushyaBlockStates.ISHUB;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.*;

public class TubeBlock extends YuushyaBlockFactory.BlockWithClassType {

    public TubeBlock(Properties properties, Integer tipLines, String classType, String autoCollision, YuushyaRegistryData.Block.Usage usage) {
        super(properties, tipLines, classType,autoCollision, usage);
        this.registerDefaultState(this.defaultBlockState().setValue(EAST,false).setValue(WEST,false).setValue(NORTH,false).setValue(SOUTH,false).setValue(UP,false).setValue(DOWN,false).setValue(ISHUB,false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(WEST).add(EAST).add(UP).add(DOWN).add(NORTH).add(SOUTH).add(AXIS).add(ISHUB);;
    }

    public BlockState transPlacement(BlockState stateIn, BlockPos currentPos, BlockGetter worldIn) {
        BlockState nblockstate = YuushyaUtils.getBlockState (worldIn.getBlockState(currentPos.north()),(LevelAccessor) worldIn,currentPos.north());
        BlockState sblockstate = YuushyaUtils.getBlockState (worldIn.getBlockState(currentPos.south()),(LevelAccessor) worldIn,currentPos.south());
        BlockState wblockstate = YuushyaUtils.getBlockState (worldIn.getBlockState(currentPos.west()),(LevelAccessor) worldIn,currentPos.west());
        BlockState eblockstate = YuushyaUtils.getBlockState (worldIn.getBlockState(currentPos.east()),(LevelAccessor) worldIn,currentPos.east());
        BlockState ublockstate = YuushyaUtils.getBlockState (worldIn.getBlockState(currentPos.above()),(LevelAccessor) worldIn,currentPos.above());
        BlockState dblockstate = YuushyaUtils.getBlockState (worldIn.getBlockState(currentPos.below()),(LevelAccessor) worldIn,currentPos.below());
        if (stateIn.getValue(ISHUB)) {
            return stateIn.setValue(NORTH, isTubeBlock(nblockstate)).setValue(SOUTH, isTubeBlock(sblockstate)).setValue(WEST, isTubeBlock(wblockstate)).setValue(EAST, isTubeBlock(eblockstate)).setValue(UP, isTubeBlock(ublockstate)).setValue(DOWN, isTubeBlock(dblockstate));
        } else {
            switch (stateIn.getValue(AXIS)) {
                case X:
                    return stateIn.setValue(NORTH, isDifferentTubeBlock(nblockstate, stateIn)).setValue(SOUTH, isDifferentTubeBlock(sblockstate, stateIn)).setValue(WEST, false).setValue(EAST, false).setValue(UP, isDifferentTubeBlock(ublockstate, stateIn)).setValue(DOWN, isDifferentTubeBlock(dblockstate, stateIn));
                case Y:
                    return stateIn.setValue(NORTH, isDifferentTubeBlock(nblockstate, stateIn)).setValue(SOUTH, isDifferentTubeBlock(sblockstate, stateIn)).setValue(WEST, isDifferentTubeBlock(wblockstate, stateIn)).setValue(EAST, isDifferentTubeBlock(eblockstate, stateIn)).setValue(UP, false).setValue(DOWN, false);
                case Z:
                    return stateIn.setValue(NORTH, false).setValue(SOUTH, false).setValue(WEST, isDifferentTubeBlock(wblockstate, stateIn)).setValue(EAST, isDifferentTubeBlock(eblockstate, stateIn)).setValue(UP, isDifferentTubeBlock(ublockstate, stateIn)).setValue(DOWN, isDifferentTubeBlock(dblockstate, stateIn));
                default:
                    throw new IllegalArgumentException();
            }
        }
    }
    public static boolean isTubeBlock(BlockState state) {
        return state.getBlock() instanceof TubeBlock;
    }
    public static boolean isDifferentTubeBlock(BlockState state1, BlockState state2) {
        return (isTubeBlock(state1) && state2.getValue(AXIS) != state1.getValue(AXIS)) || (isTubeBlock(state1) && (state1.getValue(ISHUB)));
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return transPlacement(this.defaultBlockState().setValue(AXIS, blockPlaceContext.getClickedFace().getAxis()).setValue(ISHUB,false),blockPlaceContext.getClickedPos(),blockPlaceContext.getLevel()) ;
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos){
        return transPlacement(stateIn,currentPos,worldIn);
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        switch (rot) {
            case CLOCKWISE_90 :
            case COUNTERCLOCKWISE_90:
                switch (state.getValue(AXIS)) {
                    case X:
                        return state.setValue(AXIS, Direction.Axis.Z);
                    case Z:
                        return state.setValue(AXIS, Direction.Axis.X);
                    default:
                        return state;
                }
            default : return state;
        }
    }

    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(HORIZONTAL_FACING)));
    }


}
