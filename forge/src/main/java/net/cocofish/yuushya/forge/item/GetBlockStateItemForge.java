package net.cocofish.yuushya.forge.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.cocofish.yuushya.item.showblocktool.GetBlockStateItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IItemRenderProperties;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class GetBlockStateItemForge extends GetBlockStateItem {
    public GetBlockStateItemForge(Properties properties, Integer tipLines) {
        super(properties, tipLines);
    }
    @Override
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
}
