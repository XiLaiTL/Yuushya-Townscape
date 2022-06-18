package net.cocofish.yuushya.blockentity.showblock;


import dev.architectury.hooks.block.BlockEntityHooks;
import net.cocofish.yuushya.blockentity.TransformData;
import net.cocofish.yuushya.blockentity.iTransformDataInventory;
import net.cocofish.yuushya.registries.YuushyaRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ShowBlockEntity extends BlockEntity implements iTransformDataInventory {

    private final NonNullList<TransformData> transformDatas;
    public Integer slot;
    @Override
    public NonNullList<TransformData> getTransformDatas() {return transformDatas;}

    public ShowBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(YuushyaRegistries.SHOW_BLOCK_ENTITY.get(), blockPos, blockState);
        transformDatas = NonNullList.createWithCapacity(16);
        for(int i=0;i<16;i++)transformDatas.add(new TransformData());
        slot=0;
    }
    @Override
    //readNbt
    public void load(CompoundTag compoundTag){
        super.load(compoundTag);
        iTransformDataInventory.load(compoundTag,transformDatas);
        slot= (int) compoundTag.getByte("ControlSlot");
    }
    @Override
    //writeNbt
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        iTransformDataInventory.saveAdditional(compoundTag,transformDatas);
        compoundTag.putByte("ControlSlot",slot.byteValue());
    }

    @Override
    //toInitialChunkDataNbt //When you first load world it writeNbt firstly
    public CompoundTag getUpdateTag() {
        CompoundTag compoundTag =  super.getUpdateTag();
        saveAdditional(compoundTag);
        return compoundTag;
    }

    public void saveChanged() {
        this.setChanged();
        if (this.getLevel()!=null){
            this.getLevel().setBlocksDirty(this.getBlockPos(), this.getLevel().getBlockState(this.getBlockPos()), Blocks.AIR.defaultBlockState());
        }
    }

    @NotNull
    public TransformData getTransFormDataNow(){
        return getTransformData(slot);
    }

}




//    @Override
//    public ClientboundBlockEntityDataPacket getUpdatePacket() {
//        CompoundTag compoundTag=getUpdateTag();
//        return ClientboundBlockEntityDataPacket.create(this,(blockEntity)->{saveAdditional(compoundTag);return compoundTag;});}

