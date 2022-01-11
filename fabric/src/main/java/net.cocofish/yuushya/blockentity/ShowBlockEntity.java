package net.cocofish.yuushya.blockentity;

import net.cocofish.yuushya.Yuushya;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public class ShowBlockEntity extends AbstractShowFrameBlock implements iBlockInventory {


    public ShowBlockEntity(BlockPos pos, BlockState state) {
        super(Yuushya.showblockentity,pos,state);
        blocks = DefaultedList.ofSize(1, Blocks.AIR.getDefaultState());
    }

}