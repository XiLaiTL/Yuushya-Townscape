package net.cocofish.yuushya.blockentity;

import net.cocofish.yuushya.Yuushya;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

import static net.minecraft.state.property.Properties.POWERED;

public class MixedBlockEntity extends AbstractShowFrameBlock implements iBlockInventory {

    public MixedBlockEntity(BlockPos pos, BlockState state) {
        super(Yuushya.mixedblockentity,pos,state);
        blocks = DefaultedList.ofSize(2, Blocks.AIR.getDefaultState());
    }
    public void setBasicBlock(BlockState blockState) {
        if (getBlock(0) == null) {
            setBlock(0, Blocks.AIR.getDefaultState());
        }
        setBlock(1, blockState);
        world.setBlockState(getPos(), world.getBlockState(getPos()).with(POWERED, !world.getBlockState(getPos()).get(POWERED)));
        markDirty();
    }
    public BlockState getBasicBlock(){return getBlock(1);}

}