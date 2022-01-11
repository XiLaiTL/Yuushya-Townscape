package net.cocofish.yuushya.block;

import net.cocofish.yuushya.blockstate_enum.PositionVerticalState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import static net.minecraft.block.WallMountedBlock.FACE;
import static net.minecraft.state.property.Properties.HORIZONTAL_FACING;
import static net.minecraft.state.property.Properties.POWERED;

public class TallFurnitureBlock extends IntactBlock {
    public static final EnumProperty<PositionVerticalState> POS = EnumProperty.of("pos", PositionVerticalState.class);
    public TallFurnitureBlock(Settings settings, String registname, float ambientocclusionlightlevel,int linecount) {
        super(settings, registname, ambientocclusionlightlevel,linecount);
    }
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0f, 0f, 0f, 1f, 1f, 1f);
    }
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(POWERED).add(HORIZONTAL_FACING).add(FACE).add(POS);
    }

    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        this.setDefaultState(getDefaultState().with(POS,PositionVerticalState.BOTTOM));
        world.setBlockState(pos.up(), state.with(POS,PositionVerticalState.MIDDLE), 3);
        world.setBlockState(pos.up().up(), state.with(POS,PositionVerticalState.TOP), 3);

    }
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient && player.isCreative()) {
            PositionVerticalState thispvs=(PositionVerticalState)state.get(POS);
            BlockPos blockPos1;
            BlockPos blockPos2;
            if(thispvs==PositionVerticalState.BOTTOM)
            {
                blockPos1=pos.up();
                blockPos2=pos.up().up();
            }
            else if(thispvs==PositionVerticalState.MIDDLE)
            {
                blockPos1=pos.up();
                blockPos2=pos.down();
            }
            else if(thispvs==PositionVerticalState.TOP)
            {
                blockPos1=pos.down();
                blockPos2=pos.down().down();
            }
            else
            {
                blockPos1=pos.up();
                blockPos2=pos.down();
            }
            BlockState block1=world.getBlockState(blockPos1);
            BlockState block2=world.getBlockState(blockPos2);
            if(block1.getBlock()==state.getBlock()&&block2.getBlock()==state.getBlock())
            {
                world.setBlockState(blockPos1, Blocks.AIR.getDefaultState(), 35);
                world.syncWorldEvent(player, 2001, blockPos1, Block.getRawIdFromState(block1));
                world.setBlockState(blockPos2, Blocks.AIR.getDefaultState(), 35);
                world.syncWorldEvent(player, 2001, blockPos2, Block.getRawIdFromState(block2));
            }



        }

        //super.onBlockHarvested(world, pos, state, player);

    }



}
