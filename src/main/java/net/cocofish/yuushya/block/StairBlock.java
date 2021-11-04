package net.cocofish.yuushya.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import static net.minecraft.state.property.Properties.HORIZONTAL_FACING;

public class StairBlock extends IntactBlock {
    public StairBlock(Settings settings, String registname, float ambientocclusionlightlevel,int linecount) {
        super(settings, registname,ambientocclusionlightlevel,linecount);
    }
    private static VoxelShape sshape1=Block.createCuboidShape(0.0D,0.0D,8.0D,16.0D,8.0D,16.0D);
    private static VoxelShape sshape2=Block.createCuboidShape(0.0D,8.0D,0.0D,16.0D,16.0D,8.0D);
    private static VoxelShape nshape1=Block.createCuboidShape(0.0D,8.0D,8.0D,16.0D,16.0D,16.0D);
    private static VoxelShape nshape2=Block.createCuboidShape(0.0D,0.0D,0.0D,16.0D,8.0D,8.0D);
    private static VoxelShape wshape1=Block.createCuboidShape(8.0D,8.0D,0.0D,16.0D,16.0D,16.0D);
    private static VoxelShape wshape2=Block.createCuboidShape(0.0D,0.0D,0.0D,8.0D,8.0D,16.0D);
    private static VoxelShape eshape1=Block.createCuboidShape(0.0D,8.0D,0.0D,8.0D,16.0D,16.0D);
    private static VoxelShape eshape2=Block.createCuboidShape(8.0D,0.0D,0.0D,16.0D,8.0D,16.0D);

    private static VoxelShape westshape= VoxelShapes.union(wshape1,wshape2);
    private static VoxelShape eastshape= VoxelShapes.union(eshape1,eshape2);
    private static VoxelShape southshape=VoxelShapes.union(sshape1,sshape2);
    private static VoxelShape northshape=VoxelShapes.union(nshape1,nshape2);
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
