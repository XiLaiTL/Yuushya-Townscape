package com.yuushya.block;

import com.yuushya.block.blockstate.PositionHorizonState;
import com.yuushya.block.blockstate.ShapeState;
import com.yuushya.entity.ChairEntityUtils;
import com.yuushya.registries.YuushyaRegistryData;
import com.yuushya.utils.YuushyaUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiPredicate;

import static com.yuushya.block.YuushyaBlockFactory.isTheSameFacing;
import static com.yuushya.block.YuushyaBlockFactory.isTheSameBlock;
import static com.yuushya.block.blockstate.YuushyaBlockStates.*;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class LineBlock extends AbstractYuushyaBlockType {
    public LineBlock() {
        super();
    }

    @Override
    public List<Property<?>> getBlockStateProperty() {
        return List.of(HORIZONTAL_FACING,POS_HORIZON);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        BlockState res= blockPlaceContext.getClickedFace().getAxis() == Direction.Axis.Y
                ? this.defaultBlockState().setValue(HORIZONTAL_FACING, blockPlaceContext.getHorizontalDirection())
                : this.defaultBlockState().setValue(HORIZONTAL_FACING, blockPlaceContext.getClickedFace().getOpposite());

        return res.setValue(POS_HORIZON,getPositionOfFace(res,blockPlaceContext.getLevel(),blockPlaceContext.getClickedPos(),YuushyaBlockFactory::isTheSameFacing));
    }
    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        return blockState.setValue(POS_HORIZON,getPositionOfFace(blockState,levelAccessor,blockPos,YuushyaBlockFactory::isTheSameFacing));
    }

    public static PositionHorizonState getPositionOfFace(BlockState state, LevelAccessor worldIn, BlockPos pos, BiPredicate<BlockState,BlockState> connected) {
        Direction facingDirection= state.getValue(HORIZONTAL_FACING);
        switch (facingDirection.getAxis()){
            case X -> {
                BlockState nblockstate= YuushyaUtils.getBlockState(worldIn.getBlockState(pos.north()), worldIn,pos.north());
                BlockState sblockstate=YuushyaUtils.getBlockState(worldIn.getBlockState(pos.south()),worldIn,pos.south());
                boolean nConnected=isTheSameBlock(nblockstate,state)&&connected.test(nblockstate,state);
                boolean sConnected=isTheSameBlock(sblockstate,state)&&connected.test(sblockstate,state);
                if (nConnected&&sConnected) return PositionHorizonState.MIDDLE;
                switch (facingDirection){
                    case WEST -> {
                        if(nConnected) return PositionHorizonState.LEFT;
                        if(sConnected) return PositionHorizonState.RIGHT;
                    }
                    case EAST -> {
                        if (nConnected) return PositionHorizonState.RIGHT;
                        if (sConnected) return PositionHorizonState.LEFT;
                    }
                }
            }
            case Z -> {
                BlockState wblockstate=YuushyaUtils.getBlockState(worldIn.getBlockState(pos.west()),worldIn,pos.west());
                BlockState eblockstate=YuushyaUtils.getBlockState(worldIn.getBlockState(pos.east()), worldIn,pos.east());
                boolean wConnected=isTheSameBlock(wblockstate,state)&&connected.test(wblockstate,state);
                boolean eConnected=isTheSameBlock(eblockstate,state)&&connected.test(eblockstate,state);
                if (wConnected&&eConnected) return PositionHorizonState.MIDDLE;
                switch (facingDirection) {
                    case NORTH -> {
                        if (wConnected) return PositionHorizonState.RIGHT;
                        if (eConnected) return PositionHorizonState.LEFT;
                    }
                    case SOUTH -> {
                        if (wConnected) return PositionHorizonState.LEFT;
                        if (eConnected) return PositionHorizonState.RIGHT;
                    }
                }
            }
        }
        return PositionHorizonState.NONE;
    }

}
