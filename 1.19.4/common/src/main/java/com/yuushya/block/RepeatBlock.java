package com.yuushya.block;

import com.yuushya.registries.YuushyaRegistryData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.yuushya.block.FaceBlock.getPositionOfFaceX;
import static com.yuushya.block.FaceBlock.getPositionOfFaceZ;
import static com.yuushya.block.PoleBlock.getPositionOfPole;
import static com.yuushya.block.blockstate.YuushyaBlockStates.*;

public class RepeatBlock extends AbstractYuushyaBlockType {
    public RepeatBlock() {
        super();
    }

    @Override
    public List<Property<?>> getBlockStateProperty() {
        return List.of(X,Y,Z);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        LevelAccessor levelAccessor=blockPlaceContext.getLevel();
        BlockPos blockPos=blockPlaceContext.getClickedPos();
        return this.defaultBlockState()
                .setValue(X, blockPos.getX()%12)
                .setValue(Y,blockPos.getY()%12)
                .setValue(Z, blockPos.getZ()%12);
    }
    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos){
        return stateIn
                .setValue(X, currentPos.getX()%12)
                .setValue(Y,currentPos.getY()%12)
                .setValue(Z, currentPos.getZ()%12);
    }
}
