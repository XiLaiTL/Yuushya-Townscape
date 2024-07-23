package com.yuushya.block;

import com.yuushya.block.blockstate.PositionVerticalState;
import com.yuushya.registries.YuushyaRegistryData;
import com.yuushya.utils.YuushyaUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.yuushya.block.blockstate.YuushyaBlockStates.POS_VERTICAL;
import static net.minecraft.world.level.block.Block.getId;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class TriPartBlock extends AbstractYuushyaBlockType{
    public TriPartBlock() {
        super();
    }

    @Override
    public List<Property<?>> getBlockStateProperty() {
        return List.of(HORIZONTAL_FACING,POS_VERTICAL);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockPos = context.getClickedPos();
        Level level = context.getLevel();
        if (blockPos.getY() < level.getMaxBuildHeight() - 2
                && level.getBlockState(blockPos.above()).canBeReplaced(context)
                && level.getBlockState(blockPos.above().above()).canBeReplaced(context)) {
            return this.defaultBlockState()
                    .setValue(HORIZONTAL_FACING, context.getHorizontalDirection())
                    .setValue(POS_VERTICAL,PositionVerticalState.BOTTOM );
        }
        return null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity player, ItemStack itemStack) {
        level.setBlock(blockPos.above(), blockState.setValue(POS_VERTICAL,PositionVerticalState.MIDDLE), 3);
        level.setBlock(blockPos.above().above(), blockState.setValue(POS_VERTICAL,PositionVerticalState.TOP), 3);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        PositionVerticalState doubleBlockHalf = state.getValue(POS_VERTICAL);
        if (direction.getAxis() == Direction.Axis.Y
                && ((doubleBlockHalf == PositionVerticalState.BOTTOM) && (direction == Direction.UP)
                ||(doubleBlockHalf == PositionVerticalState.TOP) && (direction == Direction.DOWN)
                || doubleBlockHalf == PositionVerticalState.MIDDLE
        )
        ) {
            if(neighborState.is(state.getBlock()) && neighborState.getValue(POS_VERTICAL) != doubleBlockHalf){
                IntegerProperty form = YuushyaUtils.getFormFromState(state);
                if(form!=null)
                    return state.setValue(HORIZONTAL_FACING, neighborState.getValue(HORIZONTAL_FACING))
                                .setValue(form, neighborState.getValue(form));
                else return state.setValue(HORIZONTAL_FACING, neighborState.getValue(HORIZONTAL_FACING));
            }

            else return Blocks.AIR.defaultBlockState();
        } else {
            return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
        }
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos blockPos, BlockState blockState, Player player) {
        if (!level.isClientSide && player.isCreative()) {
            PositionVerticalState thispvs=(PositionVerticalState)blockState.getValue(POS_VERTICAL);
            BlockPos blockPos1;
            BlockPos blockPos2;
            if (thispvs == PositionVerticalState.BOTTOM) {
                return;
            } else if (thispvs == PositionVerticalState.MIDDLE) {
                blockPos1 = blockPos.above();
                blockPos2 = blockPos.below();
            } else if (thispvs == PositionVerticalState.TOP) {
                blockPos1 = blockPos.below();
                blockPos2 = blockPos.below().below();
            } else {
                blockPos1 = blockPos.above();
                blockPos2 = blockPos.below();
            }
            BlockState block1 = level.getBlockState(blockPos1);
            BlockState block2 = level.getBlockState(blockPos2);
            if (block1.getBlock() == blockState.getBlock() && block2.getBlock() == blockState.getBlock()) {
                level.setBlock(blockPos2, Blocks.AIR.defaultBlockState(), 35);
                level.levelEvent(player, 2001, blockPos2, getId(block2));
                level.setBlock(blockPos1, Blocks.AIR.defaultBlockState(), 35);
                level.levelEvent(player, 2001, blockPos1, getId(block1));
            }
            //TODO: find the reason why it need the number when destory three part block
        }
    }

}
