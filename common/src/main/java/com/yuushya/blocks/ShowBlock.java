package com.yuushya.blocks;

import com.yuushya.BlockEntityTypes;
import com.yuushya.data.ITransformDataInventory;
import com.yuushya.data.TransformData;
import com.yuushya.mappings.BlockEntityClientSerializableMapper;
import com.yuushya.mappings.BlockEntityMapper;
import com.yuushya.mappings.EntityBlockMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED;

public class ShowBlock extends Block implements EntityBlockMapper {
    public static final IntegerProperty LIT = IntegerProperty.create("lit", 0, 15);

    public ShowBlock() {
        super(BlockBehaviour.Properties.of(Material.METAL).strength(1.0F).noOcclusion().noCollission());
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT).add(POWERED);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        TileEntityShowBlock entityShowBlock = (TileEntityShowBlock) worldIn.getBlockEntity(currentPos);
        BlockState blockState = entityShowBlock.getTransformData(0).blockState;
        Block block = blockState.getBlock();
        if (facingState.getBlock() instanceof ShowBlock) {
            return stateIn;
        }
        if (!(block instanceof AirBlock)) {
            entityShowBlock.getTransformData(0).blockState = block.updateShape(blockState, facing, facingState, worldIn, currentPos, facingPos);
            entityShowBlock.saveChanged();
            return stateIn.setValue(POWERED, !stateIn.getValue(POWERED));
        }
        return stateIn.setValue(POWERED, !stateIn.getValue(POWERED));
    }

    public static class TileEntityShowBlock extends BlockEntityClientSerializableMapper implements ITransformDataInventory {
        private static final String CONTROL_SLOT_KEY = "ControlSlot";
        private final List<TransformData> transformDatas;
        private Integer slot;

        public TileEntityShowBlock(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.SHOW_BLOCK_TILE_ENTITY.get(), pos, state);
            transformDatas = new ArrayList<>();
            transformDatas.add(new TransformData());
            slot = 0;
        }

        @Override
        public List<TransformData> getTransformDatas() {
            return transformDatas;
        }

        public TransformData getTransFormDataNow() {
            return getTransformData(slot);
        }

        public void removeTransFormDataNow() {
            removeTransformData(slot);
        }

        public void setSlotBlockStateNow(BlockState blockState) {
            setSlotBlockState(slot, blockState);
        }

        public int getSlot() {
            return slot;
        }

        public void setSlot(int slot) {
            if (slot >= transformDatas.size()) {
                for (int i = slot - transformDatas.size() + 1; i > 0; i--)
                    transformDatas.add(new TransformData());
            }
            this.slot = slot;
        }

        @Override
        public void readCompoundTag(CompoundTag compoundTag) {
            super.readCompoundTag(compoundTag);
            ITransformDataInventory.load(compoundTag, transformDatas);
            slot = (int) compoundTag.getByte(CONTROL_SLOT_KEY);
        }

        @Override
        public void writeCompoundTag(CompoundTag compoundTag) {
            super.writeCompoundTag(compoundTag);
            ITransformDataInventory.saveAdditional(compoundTag, transformDatas);
            compoundTag.putByte(CONTROL_SLOT_KEY, slot.byteValue());
        }

        public void saveChanged() {
            this.setChanged();
            if (this.getLevel() != null) {
                this.getLevel().setBlocksDirty(this.getBlockPos(), this.getLevel().getBlockState(this.getBlockPos()), Blocks.AIR.defaultBlockState());
            }
        }
    }
}