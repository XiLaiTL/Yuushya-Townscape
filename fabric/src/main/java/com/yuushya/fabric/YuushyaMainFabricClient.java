package com.yuushya.fabric;

import com.yuushya.Blocks;
import com.yuushya.YuushyaMainClient;
import com.yuushya.client.ShowBlockModelFabric;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.world.level.block.state.BlockState;

public class YuushyaMainFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        YuushyaMainClient.init();
        YuushyaMainClient.initItemModelPredicate();
        ModelLoadingRegistry.INSTANCE.registerVariantProvider(
                (resourceManager) -> (modelResourceLocation, modelProviderContext) -> {
                    for (BlockState blockState : Blocks.SHOW_BLOCK.get().getStateDefinition().getPossibleStates()) {
                        if (modelResourceLocation.equals(BlockModelShaper.stateToModelLocation(blockState))) {
                            return new ShowBlockModelFabric();
                        }
                    }
                    return null;
                }
        );
    }
}