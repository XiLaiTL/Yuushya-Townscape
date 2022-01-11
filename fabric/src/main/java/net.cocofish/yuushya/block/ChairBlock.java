package net.cocofish.yuushya.block;

import net.cocofish.yuushya.YuushyaUtils;
import net.cocofish.yuushya.blockstate_enum.PositionHorizonState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

import static net.minecraft.block.WallMountedBlock.FACE;
import static net.minecraft.block.enums.WallMountLocation.*;
import static net.minecraft.state.property.Properties.HORIZONTAL_FACING;
import static net.minecraft.state.property.Properties.POWERED;

public class ChairBlock extends AbstractYuushyaBlock{
    private YuushyaUtils YM=new YuushyaUtils();
    public static final EnumProperty<PositionHorizonState> POS = EnumProperty.of("pos",PositionHorizonState.class);
    public ChairBlock(Settings settings, String registname, float ambientocclusionlightlevel,int linecount) {
        super(settings, registname, ambientocclusionlightlevel,linecount);
        setDefaultState(getStateManager().getDefaultState().with(POS, PositionHorizonState.NONE));
    }
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0f, 0f, 0f, 1f, 1.0f, 1f);
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(POWERED).add(HORIZONTAL_FACING).add(FACE).add(POS);

    }
/*
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        if(itemStack.isEmpty()&&hand==Hand.MAIN_HAND){
            if(world.isClient) return ActionResult.CONSUME;
            ChairEntity chairEntity = new ChairEntity(world, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.0625D , (double)pos.getZ() + 0.5D);
            world.spawnEntity(chairEntity);
            chairEntity.interact(player,hand);
            return ActionResult.CONSUME;
        }
        else {
            return ActionResult.PASS;
        }

    }

 */
    public BlockState getPlacementState(ItemPlacementContext context) {
        Direction direction=context.getSide();
        BlockState blockstatereturn;
        BlockState blockstate = context.getWorld().getBlockState(context.getBlockPos().offset(direction.getOpposite()));





        //return this.getDefaultState().with(HORIZONTAL_FACING, context.getPlayerFacing()  .getOpposite());
        //else return blockstate.isIn(this) && blockstate.get(HORIZONTAL_FACING) == direction ? this.getDefaultState().with(HORIZONTAL_FACING, direction.getOpposite()) : this.getDefaultState().with(HORIZONTAL_FACING, direction);

        //Direction direction=context.getSide();

        direction=context.getPlayerFacing()  .getOpposite();
        //if(context.getNearestLookingDirection()==Direction.DOWN)
        /*
        if(direction==Direction.DOWN||direction==Direction.UP)
        {direction=context.getPlayerFacing()  .getOpposite();}
        else if(blockstate.isIn(this) && blockstate.get(HORIZONTAL_FACING) == direction)
        {direction=direction.getOpposite();}*/
        if(context.getSide()==Direction.UP)
        {
            blockstatereturn= this.getDefaultState().with(HORIZONTAL_FACING, direction).with(FACE,FLOOR);
        }
        else if(context.getSide()==Direction.DOWN)
        {
            blockstatereturn= this.getDefaultState().with(HORIZONTAL_FACING, direction).with(FACE,CEILING);
        }
        else
        {
            blockstatereturn = this.getDefaultState().with(HORIZONTAL_FACING, direction).with(FACE, WALL);
        }
        PositionHorizonState posi=getPosition(blockstatereturn,context.getWorld(),context.getBlockPos(),context.getSide().getOpposite());

        return blockstatereturn.with(POS,posi);
    }
    public PositionHorizonState getPosition(BlockState state, BlockView worldIn, BlockPos pos, Direction direction)
    {
        BlockState posblockstate=YM.getBlockState(worldIn.getBlockState(pos.offset(direction)),(WorldAccess) worldIn,pos.offset(direction));
        BlockState posblockstate2=YM.getBlockState(worldIn.getBlockState(pos.offset(direction.getOpposite())),(WorldAccess) worldIn,pos.offset(direction.getOpposite()));
        Direction currentfacing=state.get(HORIZONTAL_FACING);
        PositionHorizonState posreturn=getFacing(currentfacing,direction);
        if(posreturn== PositionHorizonState.NONE)
        {return (PositionHorizonState)state.get(POS);}
        if (isChairBlock(posblockstate) && currentfacing == posblockstate.get(HORIZONTAL_FACING)&&isSomeChair(posblockstate,state))
        { return posreturn; }
        if (isChairBlock(posblockstate2) && currentfacing == posblockstate2.get(HORIZONTAL_FACING)&&isSomeChair(posblockstate2,state)&&(PositionHorizonState) state.get(POS)== PositionHorizonState.MIDDLE)
        { return getFacing(currentfacing,direction.getOpposite()); }
        if(((!isChairBlock(posblockstate))||(isChairBlock(posblockstate)&&(!isSomeChair(posblockstate,state))))&&(((!isChairBlock(posblockstate2))||(isChairBlock(posblockstate2)&&(!isSomeChair(posblockstate2,state))))))
        {return PositionHorizonState.NONE;}
        return (PositionHorizonState)state.get(POS);
    }

    public PositionHorizonState getFacing(Direction currentfacing, Direction direction)
    {
        //direction player place face to
        //currentfacing the block facing
        switch (currentfacing)
        {
            case NORTH:
                if(direction==Direction.EAST)
                    return PositionHorizonState.RIGHT;
                else if(direction==Direction.WEST)
                    return PositionHorizonState.LEFT;
                else return PositionHorizonState.NONE;
            case SOUTH:
                if(direction==Direction.EAST)
                    return PositionHorizonState.LEFT;
                else if(direction==Direction.WEST)
                    return PositionHorizonState.RIGHT;
                else return PositionHorizonState.NONE;
            case EAST:
                if(direction==Direction.NORTH)
                    return PositionHorizonState.LEFT;
                else if(direction==Direction.SOUTH)
                    return PositionHorizonState.RIGHT;
                else return PositionHorizonState.NONE;
            case WEST:
                if(direction==Direction.NORTH)
                    return PositionHorizonState.RIGHT;
                else if(direction==Direction.SOUTH)
                    return PositionHorizonState.LEFT;
                else return PositionHorizonState.NONE;
            default:return PositionHorizonState.NONE;
        }

    }

    public boolean isSomeChair(BlockState blockState1, BlockState blockState2) {return blockState2.getBlock()==blockState1.getBlock(); }

    public static boolean isChairBlock(BlockState state) {
        return state.getBlock() instanceof ChairBlock;
    }

    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return (BlockState)state.with(HORIZONTAL_FACING, rotation.rotate((Direction)state.get(HORIZONTAL_FACING)));
    }
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation((Direction)state.get(HORIZONTAL_FACING)));
    }
    
    public BlockState getStateForNeighborUpdate(BlockState stateIn, Direction facing, BlockState facingState, WorldAccess worldIn, BlockPos currentPos, BlockPos facingPos) {
        BlockState posblock1=YM.getBlockState(worldIn.getBlockState(currentPos.offset(facing)),worldIn,currentPos.offset(facing));
        BlockState posblock2=YM.getBlockState(worldIn.getBlockState(currentPos.offset(facing.getOpposite())),worldIn,currentPos.offset(facing.getOpposite()));
        Direction currentfacing=stateIn.get(HORIZONTAL_FACING);
        if(isChairBlock(posblock1)&&isChairBlock(posblock2)&&currentfacing==posblock1.get(HORIZONTAL_FACING)&&currentfacing==posblock2.get(HORIZONTAL_FACING)&&isSomeChair(posblock1,stateIn)&&isSomeChair(posblock2,stateIn))
        {
            PositionHorizonState pos1=(PositionHorizonState) posblock1.get(POS);
            PositionHorizonState pos2=(PositionHorizonState) posblock2.get(POS);
            boolean LM1RM2 = (pos1 == PositionHorizonState.LEFT || pos1 == PositionHorizonState.MIDDLE) && (pos2 == PositionHorizonState.RIGHT || pos2 == PositionHorizonState.MIDDLE);
            boolean LM2RM1 = (pos2 == PositionHorizonState.LEFT || pos2 == PositionHorizonState.MIDDLE) && (pos1 == PositionHorizonState.RIGHT || pos1 == PositionHorizonState.MIDDLE);
            switch (currentfacing)
            {
                case WEST:
                    if(facing==Direction.NORTH)
                    {
                        if(LM1RM2)
                            return stateIn.with(POS, PositionHorizonState.MIDDLE);

                    }
                    else if(facing==Direction.SOUTH)
                    {
                        if(LM2RM1)
                            return stateIn.with(POS, PositionHorizonState.MIDDLE);
                    }
                case EAST:
                    if(facing==Direction.NORTH)
                    {
                        if(LM2RM1)
                            return stateIn.with(POS, PositionHorizonState.MIDDLE);

                    }
                    else if(facing==Direction.SOUTH)
                    {

                        if(LM1RM2)
                            return stateIn.with(POS, PositionHorizonState.MIDDLE);
                    }
                case NORTH:
                    if(facing==Direction.WEST)
                    {
                        if(LM2RM1)
                            return stateIn.with(POS, PositionHorizonState.MIDDLE);
                    }
                    else if(facing==Direction.EAST)
                    {
                        if(LM1RM2)
                            return stateIn.with(POS, PositionHorizonState.MIDDLE);
                    }
                case SOUTH:
                    if(facing==Direction.EAST)
                    {
                        if(LM2RM1)
                            return stateIn.with(POS, PositionHorizonState.MIDDLE);
                    }
                    else if(facing==Direction.WEST)
                    {
                        if(LM1RM2)
                            return stateIn.with(POS, PositionHorizonState.MIDDLE);
                    }
            }


        }
        if(getFacing(currentfacing,facing)== PositionHorizonState.NONE)
        {
            return stateIn;
        }
        return stateIn.with(POS, getPosition(stateIn, worldIn, currentPos,facing)) ;
        //return stateIn;
    }
}
