package net.cocofish.yuushya.fabric;

import net.cocofish.yuushya.YuushyaClient;
import net.cocofish.yuushya.registries.YuushyaRegistries;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.*;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class YuushyaClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        YuushyaClient.onInitializeClient();

        ModelLoadingRegistry.INSTANCE.registerVariantProvider(
                (resourceManager)->(modelResourceLocation,modelProviderContext)->{
                    if (modelResourceLocation.equals(BlockModelShaper.stateToModelLocation(YuushyaRegistries.BLOCKS.get("showblock").get().defaultBlockState()))){
                        return new ShowBlockModel();
                    }
                    return null;
                }
        );
        //BuiltinItemRendererRegistry.INSTANCE.register();
        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> {
                    if (tintIndex > -1) {
                        // decodeTintWithState
                        // 假设原tint为负数，则最高位为1，通常可以返回空气（因为不太可能出现上千万的方块状态），那么空气也不会被染色
                        BlockState trueState = Block.stateById(tintIndex >> 8);
                        int trueTint = tintIndex & 0xFF;
                        BlockColor blockColor=ColorProviderRegistry.BLOCK.get(trueState.getBlock());
                        if (blockColor==null) return 0xFFFFFFFF;
                        return blockColor.getColor(trueState,view,pos,trueTint);
                    } else {
                        return 0xFFFFFFFF;
                    }
                },
                YuushyaRegistries.SHOW_BLOCK.get());
    }
}
