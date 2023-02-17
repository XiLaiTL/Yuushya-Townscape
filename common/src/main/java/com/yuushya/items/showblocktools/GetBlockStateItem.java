package com.yuushya.items.showblocktools;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yuushya.CreativeModeTabs;
import com.yuushya.items.AbstractToolItem;
import com.yuushya.mappings.CustomNbtUtils;
import com.yuushya.mappings.Text;
import com.yuushya.showblock.ShowBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;

public class GetBlockStateItem extends AbstractToolItem {
    private BlockState blockState;
    public static final String KEY_BLOCK_STATE = "BlockState";

    /**
     * creativeModeTab: 创造物品栏
     * tipLines: 注释栏行数
     */
    public GetBlockStateItem(Function<Properties, Properties> propertiesConsumer) {
        super(CreativeModeTabs.TEST_TABS, propertiesConsumer, 3);
    }

    /**
     * tipLines: 注释栏行数
     */


    @Override
    public InteractionResult inMainHandRightClickOnBlock(Player player, BlockState blockStateTarget, Level level, BlockPos blockPos, ItemStack handItemStack) {
        if (blockStateTarget.getBlock() instanceof ShowBlock) {
            ShowBlock.ShowBlockEntity showBlockEntity = (ShowBlock.ShowBlockEntity) level.getBlockEntity(blockPos);
            BlockState blockStateShowBlock = showBlockEntity.getTransFormDataNow().blockState;

            if (!(blockStateShowBlock.getBlock() instanceof AirBlock)) {
                blockStateTarget = blockStateShowBlock;
                showBlockEntity.removeTransFormDataNow();
                showBlockEntity.saveChanged();
            } else {
                player.displayClientMessage(Text.translatable(this.getDescriptionId() + ".mainhand.pass"), true);
                return InteractionResult.PASS;
            }
        }
        blockState = blockStateTarget;
        setTag(handItemStack);
        player.displayClientMessage(Text.translatable(this.getDescriptionId() + ".mainhand.success"), true);
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult inOffHandRightClickOnBlock(Player player, BlockState blockStateTarget, Level level, BlockPos blockPos, ItemStack handItemStack) {
        getTag(handItemStack);
        if (blockState.getBlock() instanceof AirBlock) {
            player.displayClientMessage(Text.translatable(this.getDescriptionId() + ".offhand.fail"), true);
            return InteractionResult.SUCCESS;
        }
        if (blockStateTarget.getBlock() instanceof ShowBlock) {
            ShowBlock.ShowBlockEntity showBlockEntity = (ShowBlock.ShowBlockEntity) level.getBlockEntity(blockPos);
            showBlockEntity.setSlotBlockStateNow(blockState);
            showBlockEntity.setSlotShown(showBlockEntity.getSlot(), true);
            showBlockEntity.saveChanged();
            player.displayClientMessage(Text.translatable(this.getDescriptionId() + ".offhand.success"), true);
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    public void getTag(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        blockState = CustomNbtUtils.readBlockState(compoundTag.getCompound(KEY_BLOCK_STATE));
    }

    public void setTag(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        compoundTag.put(KEY_BLOCK_STATE, NbtUtils.writeBlockState(blockState));
        itemStack.setTag(compoundTag);
    }

    public static void renderByItem(ItemStack stack, ItemTransforms.TransformType mode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        BlockState blockState = CustomNbtUtils.readBlockState(stack.getOrCreateTag().getCompound(KEY_BLOCK_STATE));
        BlockRenderDispatcher blockRenderDispatcher = Minecraft.getInstance().getBlockRenderer();
        itemRenderer.render(stack, mode, false, matrices, vertexConsumers, light, overlay, blockRenderDispatcher.getBlockModel(blockState));
    }
}