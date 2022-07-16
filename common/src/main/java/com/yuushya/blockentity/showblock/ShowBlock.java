package com.yuushya.blockentity.showblock;

import com.yuushya.block.AbstractYuushyaBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.Nullable;

import static com.yuushya.block.blockstate.YuushyaBlockStates.LIT;

public class ShowBlock extends AbstractYuushyaBlock implements EntityBlock {
    public ShowBlock(Properties properties, Integer tipLines) {
        super(properties, tipLines);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ShowBlockEntity(blockPos,blockState);
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(LIT);
    }

}
