package net.cocofish.yuushya.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
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

import static net.minecraft.block.WallMountedBlock.FACE;
import static net.minecraft.block.enums.WallMountLocation.*;
import static net.minecraft.state.property.Properties.HORIZONTAL_FACING;
import static net.minecraft.state.property.Properties.POWERED;

public class SlabBlock extends AbstractYuushyaBlock {
    public SlabBlock(Settings settings, String registname, float ambientocclusionlightlevel,int linecount) {
        super(settings,registname,ambientocclusionlightlevel,linecount);
        setDefaultState(getDefaultState());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(FACE);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        //return (BlockState)this.getDefaultState().with(HORIZONTAL_FACING, ctx.getSide());
        Direction direction=context.getSide();

        if(context.getSide()==Direction.UP)
        {
            return this.getDefaultState().with(FACE,FLOOR);
        }
        else if(context.getSide()==Direction.DOWN)
        {
            return this.getDefaultState().with(FACE,CEILING);
        }
        else
        {
            BlockPos blockPos = context.getBlockPos();
            return direction != Direction.DOWN && (direction == Direction.UP || !(context.getHitPos().y - (double)blockPos.getY() > 0.5D)) ? this.getDefaultState().with(FACE,FLOOR): this.getDefaultState().with(FACE,CEILING);
            //return this.getDefaultState().with(HORIZONTAL_FACING, direction).with(FACE,WALL);
        }
    }


    private static VoxelShape floor = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
    private static VoxelShape ceiling = Block.createCuboidShape(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D);

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        switch (state.get(FACE)) {
            case FLOOR:
                        return floor;
            case CEILING:
                        return ceiling;
        default:
                return Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
        }
    }

}
