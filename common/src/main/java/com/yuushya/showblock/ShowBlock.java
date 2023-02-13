package com.yuushya.showblock;

import com.yuushya.blocks.AbstractYuushyaBlock;
import com.yuushya.mappings.BlockEntityMapper;
import com.yuushya.mappings.EntityBlockMapper;
import com.yuushya.showblock.data.ITransformDataInventory;
import com.yuushya.showblock.data.TransformData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Material;

import java.util.ArrayList;
import java.util.List;

import static com.yuushya.blocks.blockstate.YuushyaBlockStates.LIT;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED;

public class ShowBlock extends AbstractYuushyaBlock implements EntityBlockMapper {
    public ShowBlock() {
        super(Properties.of(Material.METAL).noCollission().noOcclusion().strength(4.0F).lightLevel(blockState -> blockState.getValue(LIT)), 1);
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
        ShowBlockEntity showBlockEntity = (ShowBlockEntity) worldIn.getBlockEntity(currentPos);
        BlockState blockState = showBlockEntity.getTransformData(0).blockState;
        Block block = blockState.getBlock();
        if (facingState.getBlock() instanceof ShowBlock) {
            return stateIn;
        }
        if (!(block instanceof AirBlock)) {
            showBlockEntity.getTransformData(0).blockState = block.updateShape(blockState, facing, facingState, worldIn, currentPos, facingPos);
            showBlockEntity.saveChanged();
            return stateIn.setValue(POWERED, !stateIn.getValue(POWERED));
        }
        return stateIn.setValue(POWERED, !stateIn.getValue(POWERED));
    }

    public static class ShowBlockEntity extends BlockEntityMapper implements ITransformDataInventory {
        private static final String KEY_CONTROL_SLOT = "ControlSlot";
        private final List<TransformData> transformDatas;
        private Integer slot;

        public ShowBlockEntity(BlockPos pos, BlockState state) {
            super(null, pos, state);
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

        public void setTransformDataNow(TransformData transformData) {
            setTransformData(slot, transformData);
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
            slot = (int) compoundTag.getByte(KEY_CONTROL_SLOT);
        }

        @Override
        public void writeCompoundTag(CompoundTag compoundTag) {
            super.writeCompoundTag(compoundTag);
            ITransformDataInventory.saveAdditional(compoundTag, transformDatas);
            compoundTag.putByte(KEY_CONTROL_SLOT, slot.byteValue());
        }

        public void saveChanged() {
            this.setChanged();
            if (this.getLevel() != null) {
                this.getLevel().setBlocksDirty(this.getBlockPos(), this.getLevel().getBlockState(this.getBlockPos()), Blocks.AIR.defaultBlockState());
            }
        }
    }
}