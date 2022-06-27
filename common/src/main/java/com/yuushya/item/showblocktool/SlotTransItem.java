package com.yuushya.item.showblocktool;

import com.yuushya.blockentity.showblock.ShowBlock;
import com.yuushya.blockentity.showblock.ShowBlockEntity;
import com.yuushya.item.AbstractToolItem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Consumer;

public class SlotTransItem extends AbstractToolItem {
    public SlotTransItem(Properties properties, Integer tipLines) {
        super(properties, tipLines);
    }

    @Override
    public InteractionResult inOffHandRightClickOnBlock(Player player, BlockState blockState, LevelAccessor level, BlockPos blockPos, ItemStack handItemStack){
        //左手右键 切换可见性
        return translateSlot(player,blockState,level,blockPos,handItemStack,(showBlockEntity)-> showBlockEntity.getTransFormDataNow().isShown=!showBlockEntity.getTransFormDataNow().isShown);
    }

    @Override
    public InteractionResult inMainHandRightClickOnBlock(Player player, BlockState blockState, LevelAccessor level, BlockPos blockPos, ItemStack handItemStack){
        //右手右键 向后位移切换操作层
        return translateSlot(player,blockState,level,blockPos,handItemStack,(showBlockEntity)->{
            int slot=showBlockEntity.getSlot();
            if (slot==0){
                player.displayClientMessage(new TranslatableComponent(this.getDescriptionId()+".slot.fail.min"),true);
            } else {
                showBlockEntity.setSlot(slot-1);
                player.displayClientMessage(new TranslatableComponent(this.getDescriptionId()+".slot",showBlockEntity.getSlot(),showBlockEntity.getTransFormDataNow(),showBlockEntity.getTransFormDataNow().isShown),true);
            }
        });
    }
    @Override
    public InteractionResult inMainHandLeftClickOnBlock(Player player, BlockState blockState, LevelAccessor level, BlockPos blockPos, ItemStack handItemStack){
        //右手左键 向前位移切换操作层
        return translateSlot(player,blockState,level,blockPos,handItemStack,(showBlockEntity)->{
            if (showBlockEntity.getTransFormDataNow().blockState.getBlock() instanceof AirBlock){
                player.displayClientMessage(new TranslatableComponent(this.getDescriptionId()+".slot.fail.max"),true);
            }else{
                showBlockEntity.setSlot(showBlockEntity.getSlot()+1);
                player.displayClientMessage(new TranslatableComponent(this.getDescriptionId()+".slot",showBlockEntity.getSlot(),showBlockEntity.getTransFormDataNow(),showBlockEntity.getTransFormDataNow().isShown),true);
            }
        });
    }

    protected InteractionResult translateSlot(Player player, BlockState blockState, LevelAccessor level, BlockPos blockPos, ItemStack handItemStack, Consumer<ShowBlockEntity> consumer){
        if(blockState.getBlock() instanceof ShowBlock) {
            ShowBlockEntity showBlockEntity = (ShowBlockEntity) level.getBlockEntity(blockPos);
            consumer.accept(showBlockEntity);
            showBlockEntity.saveChanged();
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

}
