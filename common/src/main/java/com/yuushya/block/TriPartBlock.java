package com.yuushya.block;

import com.yuushya.block.blockstate.PositionVerticalState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.Nullable;

import static com.yuushya.block.blockstate.YuushyaBlockStates.FORM;
import static com.yuushya.block.blockstate.YuushyaBlockStates.POS_VERTICAL;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class TriPartBlock extends NormalBlock{
    public TriPartBlock(Properties properties, Integer tipLines, String classType) {
        super(properties, tipLines, classType);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(HORIZONTAL_FACING,POS_VERTICAL, FORM);
    }
    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity player, ItemStack itemStack) {
        this.registerDefaultState(defaultBlockState().setValue(POS_VERTICAL, PositionVerticalState.BOTTOM));
        level.setBlock(blockPos.above(), blockState.setValue(POS_VERTICAL,PositionVerticalState.MIDDLE), 3);
        level.setBlock(blockPos.above().above(), blockState.setValue(POS_VERTICAL,PositionVerticalState.TOP), 3);
    }
    @Override
    public void playerWillDestroy(Level level, BlockPos blockPos, BlockState blockState, Player player) {
        if (!level.isClientSide && player.isCreative()) {
            PositionVerticalState thispvs=(PositionVerticalState)blockState.getValue(POS_VERTICAL);
            BlockPos blockPos1;
            BlockPos blockPos2;
            if(thispvs==PositionVerticalState.BOTTOM) {
                blockPos1=blockPos.above();
                blockPos2=blockPos.above().above();
            }
            else if(thispvs==PositionVerticalState.MIDDLE) {
                blockPos1=blockPos.above();
                blockPos2=blockPos.below();
            }
            else if(thispvs==PositionVerticalState.TOP) {
                blockPos1=blockPos.below();
                blockPos2=blockPos.below().below();
            }
            else {
                blockPos1=blockPos.above();
                blockPos2=blockPos.below();
            }
            BlockState block1=level.getBlockState(blockPos1);
            BlockState block2=level.getBlockState(blockPos2);
            if(block1.getBlock()==blockState.getBlock()&&block2.getBlock()==blockState.getBlock()) {
                level.setBlock(blockPos1, Blocks.AIR.defaultBlockState(), 35);
                level.setBlock(blockPos2, Blocks.AIR.defaultBlockState(), 35);
                level.levelEvent(player, 2001, blockPos1, getId(block1));
                level.levelEvent(player, 2001, blockPos2, getId(block2));
            }
            //TODO: find the reason why it need the number when destory three part block
        }
    }

}
