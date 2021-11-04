package net.cocofish.yuushya.block;

import net.cocofish.yuushya.YuushyaMethod;
import net.cocofish.yuushya.blockstate_enum.PositionHorizonState;
import net.cocofish.yuushya.blockstate_enum.PositionVerticalState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import static net.minecraft.block.WallMountedBlock.FACE;
import static net.minecraft.block.enums.WallMountLocation.*;
import static net.minecraft.block.enums.WallMountLocation.WALL;
import static net.minecraft.state.property.Properties.HORIZONTAL_FACING;
import static net.minecraft.state.property.Properties.POWERED;

public class VerticalBlock extends AbstractYuushyaBlock{
    public static final EnumProperty<PositionVerticalState> POS = EnumProperty.of("pos",PositionVerticalState.class);
    public VerticalBlock(Settings settings, String registname, float ambientocclusionlightlevel, int linecount) {
        super(settings, registname, ambientocclusionlightlevel,linecount);
        setDefaultState(this.getDefaultState().with(POWERED,false).with(POS,PositionVerticalState.NONE));
    }
    private YuushyaMethod YM=new YuushyaMethod();
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0f, 0f, 0f, 1f, 1.0f, 1f);
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(POWERED).add(HORIZONTAL_FACING).add(POS);
    }
    public BlockState getPlacementState(ItemPlacementContext context) {
        Direction direction=context.getSide();
        if(direction==Direction.DOWN||direction==Direction.UP)
        {direction=context.getPlayerFacing().getOpposite();}
        return getDefaultState().with(HORIZONTAL_FACING,direction);
    }
    public BlockState getStateForNeighborUpdate(BlockState stateIn, Direction facing, BlockState facingState, WorldAccess worldIn, BlockPos currentPos, BlockPos facingPos) {
        BlockState posUp =YM.getBlockState (worldIn.getBlockState(currentPos.up()),worldIn,currentPos.up());
        BlockState posDown =YM.getBlockState( worldIn.getBlockState(currentPos.down()),worldIn,currentPos.down());
        int count = 0;
        if (posUp.getBlock() == stateIn.getBlock() && posUp.get(HORIZONTAL_FACING) == stateIn.get(HORIZONTAL_FACING)) {
            count += 1;
        }
        if (posDown.getBlock() == stateIn.getBlock() && posDown.get(HORIZONTAL_FACING) == stateIn.get(HORIZONTAL_FACING)) {
            count -= 2;
        }
        switch (count){
            case 0:return stateIn.with(POS,PositionVerticalState.NONE);
            case 1:return stateIn.with(POS,PositionVerticalState.BOTTOM);
            case -1:return stateIn.with(POS,PositionVerticalState.MIDDLE);
            case -2:return stateIn.with(POS,PositionVerticalState.TOP);
        }
        return stateIn.with(POS,PositionVerticalState.NONE);
    }
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return (BlockState)state.with(HORIZONTAL_FACING, rotation.rotate((Direction)state.get(HORIZONTAL_FACING)));
    }
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation((Direction)state.get(HORIZONTAL_FACING)));
    }
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        //Item item = itemStack.getItem();
        //Block block = ((item instanceof BlockItem)) ? ((BlockItem)item).getBlock(): Blocks.AIR;
        if(itemStack.isEmpty()&&hand==Hand.MAIN_HAND){
            if(world.isClient) return ActionResult.CONSUME;
            if(state.get(POWERED)==false) {
                world.setBlockState(pos, (BlockState)state.with(POWERED, true), 3);
            }
            else {
                world.setBlockState(pos, (BlockState)state.with(POWERED, false), 3);
            }
            return ActionResult.CONSUME;
        }
        else {
            return ActionResult.PASS;
        }

    }
}
