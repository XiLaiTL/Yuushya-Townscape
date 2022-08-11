package com.yuushya.blockentity.showblock;

import com.yuushya.block.AbstractYuushyaBlock;
import com.yuushya.utils.YuushyaUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.Nullable;

import static com.yuushya.block.blockstate.YuushyaBlockStates.*;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED;

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
        stateBuilder.add(LIT).add(POWERED);
    }
    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos){
        ShowBlockEntity showBlockEntity= (ShowBlockEntity) worldIn.getBlockEntity(currentPos);
        BlockState blockState=showBlockEntity.getTransformData(0).blockState;
        Block block=blockState.getBlock();

        if (facingState.getBlock() instanceof ShowBlock){
            return stateIn;
            //facingState= YuushyaUtils.getBlockState(facingState,worldIn,facingPos);
        }
        if (!(block instanceof AirBlock)){
            showBlockEntity.getTransformData(0).blockState=block.updateShape(blockState,facing,facingState,worldIn,currentPos,facingPos);
            showBlockEntity.saveChanged();
            return stateIn.setValue(POWERED,!stateIn.getValue(POWERED));
        }
        return stateIn.setValue(POWERED,!stateIn.getValue(POWERED));
    }

}
