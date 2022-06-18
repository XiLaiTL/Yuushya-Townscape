package net.cocofish.yuushya.item.showblocktool;

import com.mojang.math.Vector3d;
import net.cocofish.yuushya.blockentity.TransformData;
import net.cocofish.yuushya.blockentity.showblock.ShowBlock;
import net.cocofish.yuushya.blockentity.showblock.ShowBlockEntity;
import net.cocofish.yuushya.item.AbstractToolItem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
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
            if (--showBlockEntity.slot<0)
                showBlockEntity.slot=showBlockEntity.size();
        });
    }
    @Override
    public InteractionResult inMainHandLeftClickOnBlock(Player player, BlockState blockState, LevelAccessor level, BlockPos blockPos, ItemStack handItemStack){
        //右手左键 向前位移切换操作层
        return translateSlot(player,blockState,level,blockPos,handItemStack,(showBlockEntity)->{
            if (++showBlockEntity.slot>showBlockEntity.size())
                showBlockEntity.slot=0;
        });
    }

    protected InteractionResult translateSlot(Player player, BlockState blockState, LevelAccessor level, BlockPos blockPos, ItemStack handItemStack, Consumer<ShowBlockEntity> consumer){
        if(blockState.getBlock() instanceof ShowBlock) {
            ShowBlockEntity showBlockEntity = (ShowBlockEntity) level.getBlockEntity(blockPos);
            consumer.accept(showBlockEntity);
            showBlockEntity.saveChanged();
            player.displayClientMessage(new TranslatableComponent(this.getDescriptionId()+".slot",showBlockEntity.slot,showBlockEntity.getTransFormDataNow(),showBlockEntity.getTransFormDataNow().isShown),true);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

}
