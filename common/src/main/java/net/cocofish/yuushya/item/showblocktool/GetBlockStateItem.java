package net.cocofish.yuushya.item.showblocktool;

import net.cocofish.yuushya.blockentity.showblock.ShowBlock;
import net.cocofish.yuushya.blockentity.showblock.ShowBlockEntity;
import net.cocofish.yuushya.item.AbstractToolItem;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class GetBlockStateItem extends AbstractToolItem {
    private BlockState blockState;
    public GetBlockStateItem(Properties properties, Integer tipLines) {
        super(properties, tipLines);
        blockState = Blocks.AIR.defaultBlockState();
    }

    @Override
    public InteractionResult inMainHandRightClickOnBlock(Player player, BlockState blockStateTarget, LevelAccessor level, BlockPos blockPos, ItemStack handItemStack){
        //右手右键复制状态，以及清空展示方块内的东西//with main hand right-click can read
        if(blockStateTarget.getBlock() instanceof ShowBlock){
            ShowBlockEntity showBlockEntity = (ShowBlockEntity) level.getBlockEntity(blockPos);
            BlockState blockStateShowBlock =showBlockEntity.getTransFormDataNow().blockState;
            if (!(blockStateShowBlock.getBlock() instanceof AirBlock)){
                blockStateTarget=blockStateShowBlock;
                showBlockEntity.removeSlotBlockState(showBlockEntity.slot);
                showBlockEntity.saveChanged();
            }
            else {
                player.displayClientMessage(new TranslatableComponent(this.getDescriptionId()+".mainhand.pass"), true);
                return InteractionResult.PASS;
            }
        }
        blockState = blockStateTarget;
        setTag(handItemStack);
        player.displayClientMessage(new TranslatableComponent(this.getDescriptionId()+".mainhand.success"),true);
        return InteractionResult.SUCCESS;
    }
    @Override
    public InteractionResult inOffHandRightClickOnBlock(Player player, BlockState blockStateTarget, LevelAccessor level, BlockPos blockPos, ItemStack handItemStack){
        //左手右键放置状态到展示方块里//with off hand right-click can put blockstate to showblock
        getTag(handItemStack);
        if(blockState.getBlock() instanceof AirBlock){
            player.displayClientMessage(new TranslatableComponent(this.getDescriptionId()+".offhand.fail"), true);
            return InteractionResult.SUCCESS;
        }
        if(blockStateTarget.getBlock() instanceof ShowBlock){
            ShowBlockEntity showBlockEntity = (ShowBlockEntity) level.getBlockEntity(blockPos);
            showBlockEntity.setSlotBlockState(showBlockEntity.slot,blockState);
            showBlockEntity.setSlotShown(showBlockEntity.slot,true);
            showBlockEntity.saveChanged();
            player.displayClientMessage(new TranslatableComponent(this.getDescriptionId()+".offhand.success"), true);
            return InteractionResult.SUCCESS;
        } else {return InteractionResult.PASS;}
    }

    //method for readNbt and writeNbt
    public void getTag(ItemStack itemStack){
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        blockState = NbtUtils.readBlockState(compoundTag.getCompound("BlockState"));
    }
    public void setTag(ItemStack itemStack){
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        compoundTag.put("BlockState",NbtUtils.writeBlockState(blockState));
        itemStack.setTag(compoundTag);
    }

}
