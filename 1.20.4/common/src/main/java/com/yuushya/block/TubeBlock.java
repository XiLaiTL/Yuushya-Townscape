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
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.yuushya.block.YuushyaBlockFactory.isTheSameBlock;
import static com.yuushya.block.blockstate.YuushyaBlockStates.ISHUB;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.*;

public class TubeBlock extends AbstractYuushyaBlockType {

    public TubeBlock() {}

    @Override
    public List<Property<?>> getBlockStateProperty() {
        return List.of(WEST,EAST,UP,DOWN,NORTH,SOUTH,AXIS,ISHUB);
    }

    public static BlockState transPlacement(BlockState stateIn, BlockPos currentPos, BlockGetter worldIn) {
        BlockState nblockstate = YuushyaUtils.getBlockState (worldIn.getBlockState(currentPos.north()),(LevelAccessor) worldIn,currentPos.north());
        BlockState sblockstate = YuushyaUtils.getBlockState (worldIn.getBlockState(currentPos.south()),(LevelAccessor) worldIn,currentPos.south());
        BlockState wblockstate = YuushyaUtils.getBlockState (worldIn.getBlockState(currentPos.west()),(LevelAccessor) worldIn,currentPos.west());
        BlockState eblockstate = YuushyaUtils.getBlockState (worldIn.getBlockState(currentPos.east()),(LevelAccessor) worldIn,currentPos.east());
        BlockState ublockstate = YuushyaUtils.getBlockState (worldIn.getBlockState(currentPos.above()),(LevelAccessor) worldIn,currentPos.above());
        BlockState dblockstate = YuushyaUtils.getBlockState (worldIn.getBlockState(currentPos.below()),(LevelAccessor) worldIn,currentPos.below());
        if (stateIn.getValue(ISHUB)) {
            return stateIn.setValue(NORTH, isTheSameBlock(stateIn,nblockstate))
                    .setValue(SOUTH, isTheSameBlock(stateIn,sblockstate))
                    .setValue(WEST, isTheSameBlock(stateIn,wblockstate))
                    .setValue(EAST, isTheSameBlock(stateIn,eblockstate))
                    .setValue(UP, isTheSameBlock(stateIn,ublockstate))
                    .setValue(DOWN, isTheSameBlock(stateIn,dblockstate));
        } else {
            return switch (stateIn.getValue(AXIS)) {
                case X -> stateIn.setValue(NORTH, isDifferentTubeBlock(nblockstate, stateIn)).setValue(SOUTH, isDifferentTubeBlock(sblockstate, stateIn)).setValue(WEST, false).setValue(EAST, false).setValue(UP, isDifferentTubeBlock(ublockstate, stateIn)).setValue(DOWN, isDifferentTubeBlock(dblockstate, stateIn));
                case Y -> stateIn.setValue(NORTH, isDifferentTubeBlock(nblockstate, stateIn)).setValue(SOUTH, isDifferentTubeBlock(sblockstate, stateIn)).setValue(WEST, isDifferentTubeBlock(wblockstate, stateIn)).setValue(EAST, isDifferentTubeBlock(eblockstate, stateIn)).setValue(UP, false).setValue(DOWN, false);
                case Z -> stateIn.setValue(NORTH, false).setValue(SOUTH, false).setValue(WEST, isDifferentTubeBlock(wblockstate, stateIn)).setValue(EAST, isDifferentTubeBlock(eblockstate, stateIn)).setValue(UP, isDifferentTubeBlock(ublockstate, stateIn)).setValue(DOWN, isDifferentTubeBlock(dblockstate, stateIn));
            };
        }
    }
    public static boolean isDifferentTubeBlock(BlockState state1, BlockState state2) {
        return (isTheSameBlock (state1,state2) && state2.getValue(AXIS) != state1.getValue(AXIS)) || (isTheSameBlock (state1,state2) && (state1.getValue(ISHUB)));
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
        return switch (rot) {
            case COUNTERCLOCKWISE_90, CLOCKWISE_90 -> switch (state.getValue(AXIS)) {
                case X -> state.setValue(AXIS, Direction.Axis.Z);
                case Z -> state.setValue(AXIS, Direction.Axis.X);
                default -> state;
            };
            default -> state;
        };
    }

    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(HORIZONTAL_FACING)));
    }


}
