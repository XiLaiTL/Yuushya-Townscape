package net.cocofish.yuushya.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

import static net.minecraft.state.property.Properties.HORIZONTAL_FACING;

public class SemiBlock extends IntactBlock {
    public SemiBlock(Settings settings, String registname, float ambientocclusionlightlevel,int linecount) {
        super(settings, registname,ambientocclusionlightlevel,linecount);
    }
    private static VoxelShape westshape=Block.createCuboidShape(8.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static VoxelShape eastshape=Block.createCuboidShape(0.0D, 0.0D, 0.0D, 8.0D, 16.0D, 16.0D);
    private static VoxelShape southshape=Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 8.0D);
    private static VoxelShape northshape=Block.createCuboidShape(0.0D, 0.0D, 8.0D, 16.0D, 16.0D, 16.0D);
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

}
