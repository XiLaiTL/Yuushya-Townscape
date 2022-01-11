package net.cocofish.yuushya.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class PlatformBlock extends ChairBlock {
    public PlatformBlock(Settings settings, String registname, float ambientocclusionlightlevel,int linecount) {
        super(settings, registname,ambientocclusionlightlevel,linecount);
    }
    private static  VoxelShape normal=Block.createCuboidShape(0.0D, 15.0D, 0.0D, 16.0D, 16.0D, 16.0D);

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return normal;
    }

}
