package com.yuushya.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yuushya.items.showblocktools.GetBlockStateItem;
#if MC_VERSION >= "11902"
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
#else
import net.minecraftforge.client.IItemRenderProperties;
#endif

import java.util.function.Consumer;

public class GetBlockStateItemForge extends GetBlockStateItem {
    @Override
    #if MC_VERSION >= "11902"
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new BlockEntityWithoutLevelRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()) {
                    @Override
                    public void renderByItem(ItemStack stack, ItemTransforms.TransformType mode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
                        GetBlockStateItem.renderByItem(stack, mode, matrices, vertexConsumers, light, overlay);
                    }
                };
            }
        });
    }
    #else
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return new BlockEntityWithoutLevelRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(),Minecraft.getInstance().getEntityModels()){
                    @Override
                    public void renderByItem(@NotNull ItemStack stack, ItemTransforms.@NotNull TransformType mode, @NotNull PoseStack matrices, @NotNull MultiBufferSource vertexConsumers, int light, int overlay) {
                        GetBlockStateItem .renderByItem(stack,mode,matrices,vertexConsumers,light,overlay);
                    }
                };
            }
        });
    }
    #endif
}
