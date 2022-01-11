package net.cocofish.yuushya.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.enums.SlabType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

import static net.minecraft.block.WallMountedBlock.FACE;
import static net.minecraft.block.enums.WallMountLocation.*;
import static net.minecraft.state.property.Properties.HORIZONTAL_FACING;

public class BeamBlock extends IntactBlock {
    public BeamBlock(Settings settings, String registname, float ambientocclusionlightlevel,int linecount) {
        super(settings, registname, ambientocclusionlightlevel,linecount);
    }

    private static final VoxelShape westshape1 = Block.createCuboidShape(8.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
    private static final VoxelShape eastshape1 = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 8.0D, 8.0D, 16.0D);
    private static final VoxelShape southshape1 = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 8.0D);
    private static final VoxelShape northshape1 = Block.createCuboidShape(0.0D, 0.0D, 8.0D, 16.0D, 8.0D, 16.0D);
    private static final VoxelShape westshape2 = Block.createCuboidShape(8.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape eastshape2 = Block.createCuboidShape(0.0D, 8.0D, 0.0D, 8.0D, 16.0D, 16.0D);
    private static final VoxelShape southshape2 = Block.createCuboidShape(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 8.0D);
    private static final VoxelShape northshape2 = Block.createCuboidShape(0.0D, 8.0D, 8.0D, 16.0D, 16.0D, 16.0D);

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        switch (state.get(FACE)) {
            case FLOOR:
                switch (state.get(HORIZONTAL_FACING)) {
                    case WEST:
                        return westshape1;
                    case EAST:
                        return eastshape1;
                    case NORTH:
                        return northshape1;
                    case SOUTH:
                        return southshape1;
                    default:
                        return southshape1;
                }
            case CEILING:
                switch (state.get(HORIZONTAL_FACING)) {
                    case WEST:
                        return westshape2;
                    case EAST:
                        return eastshape2;
                    case NORTH:
                        return northshape2;
                    case SOUTH:
                        return southshape2;
                    default:
                        return southshape2;
                }
            default:
                return Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        //return (BlockState)this.getDefaultState().with(HORIZONTAL_FACING, ctx.getSide());
        Direction direction=context.getSide();
        BlockState blockstate = context.getWorld().getBlockState(context.getBlockPos().offset(direction.getOpposite()));
        if(direction==Direction.DOWN||direction==Direction.UP)
        {direction=context.getPlayerFacing().getOpposite();}

        else if(blockstate.isOf(this) && blockstate.get(HORIZONTAL_FACING) == direction)
        {direction=direction.getOpposite();}

        if(context.getSide()==Direction.UP)
        {
            return this.getDefaultState().with(HORIZONTAL_FACING, direction).with(FACE,FLOOR);
        }
        else if(context.getSide()==Direction.DOWN)
        {
            return this.getDefaultState().with(HORIZONTAL_FACING, direction).with(FACE,CEILING);
        }
        else
        {
            BlockPos blockPos = context.getBlockPos();
            return direction != Direction.DOWN && (direction == Direction.UP || !(context.getHitPos().y - (double)blockPos.getY() > 0.5D)) ? this.getDefaultState().with(HORIZONTAL_FACING, direction).with(FACE,FLOOR): this.getDefaultState().with(HORIZONTAL_FACING, direction).with(FACE,CEILING);
            //return this.getDefaultState().with(HORIZONTAL_FACING, direction).with(FACE,WALL);
        }
    }


}
