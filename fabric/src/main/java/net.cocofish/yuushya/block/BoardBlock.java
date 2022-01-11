package net.cocofish.yuushya.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

import static net.minecraft.block.WallMountedBlock.FACE;
import static net.minecraft.state.property.Properties.HORIZONTAL_FACING;
import static net.minecraft.state.property.Properties.POWERED;

public class BoardBlock extends IntactBlock {
    public BoardBlock(Settings settings, String registname, float ambientocclusionlightlevel,int linecount) {
        super(settings, registname, ambientocclusionlightlevel,linecount);
    }
    private static VoxelShape westshape=Block.createCuboidShape(15.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static VoxelShape eastshape=Block.createCuboidShape(0.0D, 0.0D, 0.0D, 1.0D, 16.0D, 16.0D);
    private static VoxelShape southshape=Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 1.0D);
    private static VoxelShape northshape=Block.createCuboidShape(0.0D, 0.0D, 15.0D, 16.0D, 16.0D, 16.0D);
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        switch (state.get(HORIZONTAL_FACING))
        {
            case WEST:return westshape;
            case EAST:return eastshape;
            case NORTH:return northshape;
            case SOUTH:return southshape;
            default:return southshape;
        }
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(POWERED).add(HORIZONTAL_FACING).add(FACE).add(FORM);
    }

}
