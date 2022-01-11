package net.cocofish.yuushya.block;

import net.cocofish.yuushya.YuushyaUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;


import static net.minecraft.state.property.Properties.*;

public class TubeBlock extends AbstractYuushyaBlock {
    private boolean isHub;
    public static final BooleanProperty ISHUB = BooleanProperty.of("ishub");
    public TubeBlock(Settings settings, String registname, float ambientocclusionlightlevel,boolean ishub,int linecount) {
        super(settings, registname, ambientocclusionlightlevel,linecount);
        setDefaultState(getStateManager().getDefaultState().with(EAST,false).with(WEST,false).with(NORTH,false).with(SOUTH,false).with(UP,false).with(DOWN,false).with(ISHUB,false));
        isHub=ishub;
    }
    private YuushyaUtils YM=new YuushyaUtils();
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(WEST).add(EAST).add(UP).add(DOWN).add(NORTH).add(SOUTH).add(AXIS).add(ISHUB);
    }
    public static boolean isTubeBlock(BlockState state) {
        return state.getBlock() instanceof TubeBlock;
    }

    private static boolean isDifferetAxis(BlockState state1, BlockState state2) {
        return state2.get(AXIS) != state1.get(AXIS);
    }

    public static boolean isDifferentTubeBlock(BlockState state1, BlockState state2) {
        return (isTubeBlock(state1) && isDifferetAxis(state1, state2)) || (isTubeBlock(state1) && (state1.get(ISHUB) == true));
    }

    public BlockState transPlacement(BlockState stateIn, BlockPos currentPos, BlockView worldIn) {
        BlockState nblockstate = YM.getBlockState (worldIn.getBlockState(currentPos.north()),(WorldAccess) worldIn,currentPos.north());
        BlockState sblockstate = YM.getBlockState (worldIn.getBlockState(currentPos.south()),(WorldAccess) worldIn,currentPos.south());
        BlockState wblockstate = YM.getBlockState (worldIn.getBlockState(currentPos.west()),(WorldAccess) worldIn,currentPos.west());
        BlockState eblockstate = YM.getBlockState (worldIn.getBlockState(currentPos.east()),(WorldAccess) worldIn,currentPos.east());
        BlockState ublockstate = YM.getBlockState (worldIn.getBlockState(currentPos.up()),(WorldAccess) worldIn,currentPos.up());
        BlockState dblockstate = YM.getBlockState (worldIn.getBlockState(currentPos.down()),(WorldAccess) worldIn,currentPos.down());
        if (stateIn.get(ISHUB)) {
            return stateIn.with(NORTH, isTubeBlock(nblockstate)).with(SOUTH, isTubeBlock(sblockstate)).with(WEST, isTubeBlock(wblockstate)).with(EAST, isTubeBlock(eblockstate)).with(UP, isTubeBlock(ublockstate)).with(DOWN, isTubeBlock(dblockstate));
        } else {
            if (stateIn.get(AXIS) == Direction.Axis.X) {
                return stateIn.with(NORTH, isDifferentTubeBlock(nblockstate, stateIn)).with(SOUTH, isDifferentTubeBlock(sblockstate, stateIn)).with(WEST, false).with(EAST, false).with(UP, isDifferentTubeBlock(ublockstate, stateIn)).with(DOWN, isDifferentTubeBlock(dblockstate, stateIn));
            } else if (stateIn.get(AXIS) == Direction.Axis.Y) {
                return stateIn.with(NORTH, isDifferentTubeBlock(nblockstate, stateIn)).with(SOUTH, isDifferentTubeBlock(sblockstate, stateIn)).with(WEST, isDifferentTubeBlock(wblockstate, stateIn)).with(EAST, isDifferentTubeBlock(eblockstate, stateIn)).with(UP, false).with(DOWN, false);
            } else if (stateIn.get(AXIS) == Direction.Axis.Z) {
                return stateIn.with(NORTH, false).with(SOUTH, false).with(WEST, isDifferentTubeBlock(wblockstate, stateIn)).with(EAST, isDifferentTubeBlock(eblockstate, stateIn)).with(UP, isDifferentTubeBlock(ublockstate, stateIn)).with(DOWN, isDifferentTubeBlock(dblockstate, stateIn));
            }
        }

        return stateIn.with(NORTH, isTubeBlock(nblockstate)).with(SOUTH, isTubeBlock(sblockstate)).with(WEST, isTubeBlock(wblockstate)).with(EAST, isTubeBlock(eblockstate)).with(UP, isTubeBlock(ublockstate)).with(DOWN, isTubeBlock(dblockstate));

    }

    public BlockState rotate(BlockState state, BlockRotation rot) {
        switch(rot) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                switch((Direction.Axis)state.get(AXIS)) {
                    case X:
                        return state.with(AXIS, Direction.Axis.Z);
                    case Z:
                        return state.with(AXIS, Direction.Axis.X);
                    default:
                        return state;
                }
            default:
                return state;
        }
    }

    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation((Direction) state.get(HORIZONTAL_FACING)));
    }

    public BlockState getPlacementState(ItemPlacementContext context) {
        if(isHub)
        {
            return transPlacement(this.getDefaultState().with(AXIS, context.getSide().getAxis()).with(ISHUB,true),context.getBlockPos(),context.getWorld()) ;
        }
        else
        {
            return transPlacement(this.getDefaultState().with(AXIS, context.getSide().getAxis()).with(ISHUB,false),context.getBlockPos(),context.getWorld()) ;
        }
    }

    public BlockState getStateForNeighborUpdate(BlockState stateIn, Direction facing, BlockState facingState, WorldAccess worldIn, BlockPos currentPos, BlockPos facingPos) {
        return transPlacement(stateIn,currentPos,worldIn);
    }
}