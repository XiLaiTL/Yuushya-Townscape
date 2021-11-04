package net.cocofish.yuushya;

import net.cocofish.yuushya.blockentity.MixedBlockEntityRender;
import net.cocofish.yuushya.blockentity.ModelFrameBlockEntityRender;
import net.cocofish.yuushya.blockentity.ShelfBlockEntityRender;
import net.cocofish.yuushya.blockentity.ShowBlockEntityRender;
import net.cocofish.yuushya.entity.ChairEntityRender;
import net.cocofish.yuushya.entity.ExhibitionEntityRender;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import static net.cocofish.yuushya.Yuushya.*;

@Environment(EnvType.CLIENT)
public class YuushyaClient implements ClientModInitializer {
    public YuushyaElements yuushyaElements=Yuushya.yuushyaElements;
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(exhibitionEntity, (dispatcher, context) -> {
            return new ExhibitionEntityRender(dispatcher);
        });
        EntityRendererRegistry.INSTANCE.register(chairEntity, (dispatcher, context) -> {
            return new ChairEntityRender(dispatcher);
        });
        BlockEntityRendererRegistry.INSTANCE.register(modelframeblockentity, ModelFrameBlockEntityRender::new);
        BlockEntityRendererRegistry.INSTANCE.register(shelfblockentity, ShelfBlockEntityRender::new);
        BlockEntityRendererRegistry.INSTANCE.register(showblockentity, ShowBlockEntityRender::new);
        BlockEntityRendererRegistry.INSTANCE.register(mixedblockentity, MixedBlockEntityRender::new);

        FabricModelPredicateProviderRegistry.register(new Identifier("direction"), (itemStack, clientWorld, livingEntity) -> {
            return itemStack.hasTag() ? itemStack.getTag().getInt("TransDirection") : 0;
        });
    }
}
