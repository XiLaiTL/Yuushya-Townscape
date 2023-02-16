package com.yuushya.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yuushya.items.showblocktools.GetBlockStateItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
#if MC_VERSION >= "11902"
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
#elif MC_VERSION >= "11701"
import net.minecraftforge.client.IItemRenderProperties;
#else
#endif

import java.util.function.Consumer;
import java.util.function.Function;

public class GetBlockStateItemForge extends GetBlockStateItem {


    #if MC_VERSION >= "11902"
    public GetBlockStateItemForge() {
         super(properties -> properties.stacksTo(1));
    }
    @Override
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
    #elif MC_VERSION >= "11701"
    public GetBlockStateItemForge() {
         super(properties -> properties.stacksTo(1));
    }
    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return new BlockEntityWithoutLevelRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(),Minecraft.getInstance().getEntityModels()){
                    @Override
                    public void renderByItem(ItemStack stack, ItemTransforms.TransformType mode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
                        GetBlockStateItem .renderByItem(stack,mode,matrices,vertexConsumers,light,overlay);
                    }
                };
            }
        });
    }
    #else
    public GetBlockStateItemForge() {
        super(properties -> properties.setISTER(() -> () -> new BlockEntityWithoutLevelRenderer(){
            @Override
            public void renderByItem(ItemStack stack, ItemTransforms.TransformType mode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
                GetBlockStateItem.renderByItem(stack, mode, matrices, vertexConsumers, light, overlay);
            }
        }));
    }
    #endif
}
