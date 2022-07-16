package com.yuushya.block;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import org.jetbrains.annotations.Nullable;

import static com.yuushya.block.blockstate.YuushyaBlockStates.FORM;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.ATTACH_FACE;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class NormalBlock extends YuushyaBlockFactory.BlockWithClassType {
    public NormalBlock(Properties properties, Integer tipLines, String classType) {
        super(properties, tipLines, classType);
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(HORIZONTAL_FACING,ATTACH_FACE, FORM);
    }
    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        //from FaceAttachedHorizontalDirectionalBlock
        Direction direction = blockPlaceContext.getNearestLookingDirection();
        return direction.getAxis() == Direction.Axis.Y
                ? this.defaultBlockState()
                .setValue(ATTACH_FACE, direction == Direction.UP ? AttachFace.CEILING : AttachFace.FLOOR)
                .setValue(HORIZONTAL_FACING, blockPlaceContext.getHorizontalDirection())
                : this.defaultBlockState()
                .setValue(ATTACH_FACE, AttachFace.WALL)
                .setValue(HORIZONTAL_FACING, direction.getOpposite());
    }
}