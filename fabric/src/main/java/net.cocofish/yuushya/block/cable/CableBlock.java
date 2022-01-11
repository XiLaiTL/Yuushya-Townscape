package net.cocofish.yuushya.block.cable;

import net.cocofish.yuushya.YuushyaUtils;
import net.cocofish.yuushya.block.AbstractYuushyaBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

import static net.minecraft.state.property.Properties.*;

public class CableBlock extends AbstractYuushyaBlock {

    public CableBlock(Settings settings, String registname, float ambientocclusionlightlevel, int linecount) {
        super(settings, registname, ambientocclusionlightlevel, linecount);
        setDefaultState(this.getDefaultState().with(DISTANCE,15).with(ISEND,false));
    }
    public static final IntProperty DISTANCE=IntProperty.of("distance",0,15);
    public static final BooleanProperty ISEND=BooleanProperty.of("isend");
    private int fromNW=0;
    private int fromSE=0;
    private boolean isN2S=true;
    private static YuushyaUtils YM=new YuushyaUtils();
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(HORIZONTAL_FACING).add(DISTANCE).add(ISEND);
    }
    public static BlockState updateDistanceFromPost(BlockState state, WorldAccess world, BlockPos pos){
        CableBlock thiscableblock=(CableBlock) state.getBlock();
        thiscableblock.setNorthSouth(state.get(HORIZONTAL_FACING)==Direction.NORTH||state.get(HORIZONTAL_FACING)==Direction.SOUTH);
        BlockState nblockstate=YM.getBlockState(world.getBlockState(pos.north()),world,pos.north());
        BlockState sblockstate=YM.getBlockState(world.getBlockState(pos.south()),world,pos.south());
        BlockState wblockstate=YM.getBlockState(world.getBlockState(pos.west()),world,pos.west());
        BlockState eblockstate=YM.getBlockState(world.getBlockState(pos.east()),world,pos.east());

        Direction ndirection=nblockstate.getBlock() instanceof CableBlock ?nblockstate.get(HORIZONTAL_FACING):state.get(HORIZONTAL_FACING);
        Direction sdirection=sblockstate.getBlock() instanceof CableBlock ?sblockstate.get(HORIZONTAL_FACING):state.get(HORIZONTAL_FACING);
        Direction wdirection=wblockstate.getBlock() instanceof CableBlock ?wblockstate.get(HORIZONTAL_FACING):state.get(HORIZONTAL_FACING);
        Direction edirection=eblockstate.getBlock() instanceof CableBlock ?eblockstate.get(HORIZONTAL_FACING):state.get(HORIZONTAL_FACING);

        Direction direction=state.get(HORIZONTAL_FACING);
        if(thiscableblock.isN2S){
            thiscableblock.fromNW = thiscableblock.getDistanceFromPost(nblockstate, state);
            thiscableblock.fromSE = thiscableblock.getDistanceFromPost(sblockstate, state);
            if(nblockstate.getBlock() instanceof CablePostBlock) direction=Direction.NORTH;
            else if(sblockstate.getBlock() instanceof CablePostBlock) direction=Direction.SOUTH;
            else {direction = thiscableblock.fromNW < thiscableblock.fromSE ? ndirection : sdirection;}
        }
        else {
            thiscableblock.fromNW = thiscableblock.getDistanceFromPost(wblockstate, state);
            thiscableblock.fromSE = thiscableblock.getDistanceFromPost(eblockstate, state);
            if(wblockstate.getBlock() instanceof CablePostBlock) direction=Direction.WEST;
            else if(eblockstate.getBlock() instanceof CablePostBlock) direction=Direction.EAST;
            else {direction = thiscableblock.fromNW < thiscableblock.fromSE ? wdirection : edirection;}
        }
        int distance=Math.min(thiscableblock.fromNW,thiscableblock.fromSE);
        boolean isend=thiscableblock.fromNW== thiscableblock.fromSE||Math.abs(thiscableblock.fromNW- thiscableblock.fromSE)==1;

        //if(thiscableblock.fromNW== thiscableblock.fromSE||Math.abs(thiscableblock.fromNW- thiscableblock.fromSE)==1)
        return state.with(DISTANCE,distance).with(ISEND,isend).with(HORIZONTAL_FACING,direction);


    }
    public int getDistanceFromPost(BlockState blockState,BlockState thisState){
        if(blockState.getBlock() instanceof CablePostBlock) {
            return 0;
        }
        else if(blockState.getBlock() instanceof CableBlock){
            if(differFacing(blockState,thisState)!=0)
            {
                return blockState.get(DISTANCE)+1>15?15:blockState.get(DISTANCE)+1;
            }
        }

        return 15;
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction=ctx.getSide();
        if(direction==Direction.DOWN||direction==Direction.UP)
        {direction=ctx.getPlayerFacing().getOpposite();}
        return updateDistanceFromPost((BlockState)this.getDefaultState().with(HORIZONTAL_FACING,direction), ctx.getWorld(), ctx.getBlockPos());
    }
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
//        int i = getDistanceFromLog(newState) + 1;
//        if (i != 1 || (Integer)state.get(DISTANCE) != i) {
//            world.getBlockTickScheduler().schedule(pos, this, 1);
//        }

        return updateDistanceFromPost((BlockState)this.getDefaultState().with(HORIZONTAL_FACING,state.get(HORIZONTAL_FACING)), world, pos);
    }
    public void setNorthSouth(boolean iss){
        isN2S=iss;
    }

    public int differFacing(BlockState blockState,BlockState thisState){
        Direction other=(blockState.get(HORIZONTAL_FACING)==null)?(blockState.get(FACING)==null?null:blockState.get(FACING)):blockState.get(HORIZONTAL_FACING);
        Direction thiss=(thisState.get(HORIZONTAL_FACING)==null)?(thisState.get(FACING)==null?null:thisState.get(FACING)):thisState.get(HORIZONTAL_FACING);
        if(other==thiss) return 2;
        else if(other==thiss.getOpposite()) return 1;
        else return 0;

    }




}
