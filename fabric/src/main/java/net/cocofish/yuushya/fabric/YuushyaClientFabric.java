package net.cocofish.yuushya.fabric;

import net.cocofish.yuushya.YuushyaClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class YuushyaClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        YuushyaClient.onInitializeClient();
        ModelLoadingRegistry.INSTANCE.registerResourceProvider(
                resourceManager->new ModelResourceProvider() {
                    @Override
                    public @Nullable UnbakedModel loadModelResource(ResourceLocation resourceId, ModelProviderContext context) throws ModelProviderException {
                        return null;
                    }
                }
        );
        //BuiltinItemRendererRegistry.INSTANCE.register();
    }
}
