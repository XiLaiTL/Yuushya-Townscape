package net.cocofish.yuushya;

import net.cocofish.yuushya.blockentity.MixedBlockEntityRender;
import net.cocofish.yuushya.blockentity.ModelFrameBlockEntityRender;
import net.cocofish.yuushya.blockentity.ShelfBlockEntityRender;
import net.cocofish.yuushya.blockentity.ShowBlockEntityRender;
import net.cocofish.yuushya.entity.ChairEntity;
import net.cocofish.yuushya.entity.ChairEntityRender;
import net.cocofish.yuushya.entity.ExhibitionEntity;
import net.cocofish.yuushya.entity.ExhibitionEntityRender;
import net.cocofish.yuushya.particle.YuushyaBlockParticle;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.EntityType;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import static net.cocofish.yuushya.Yuushya.*;

@Environment(EnvType.CLIENT)
public class YuushyaClient implements ClientModInitializer {
    public YuushyaElements yuushyaElements=Yuushya.yuushyaElements;
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(modelframeblock, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(showblock, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(mixedblock, RenderLayer.getCutout());
        //BlockRenderLayerMap.INSTANCE.putBlock(leafparticleblock, RenderLayer.getCutout());
        ParticleFactoryRegistry.getInstance().register(leafparticle, YuushyaBlockParticle.Factory::new);

        yuushyaElements.yuushyablock.forEach((name,yuushyaBlock)->{
            yuushyaElements.clientRegister(yuushyaBlock,yuushyaElements.blocksregister.get(yuushyaBlock.name));
        });
//        EntityRendererRegistry.INSTANCE.register((EntityType<? extends ExhibitionEntity>) exhibitionEntity, (context) -> {
//            return new ExhibitionEntityRender(context);
//        });
//        EntityRendererRegistry.INSTANCE.register((EntityType<? extends ChairEntity>) chairEntity, (context) -> {
//            return new ChairEntityRender(context);
//        });
        BlockEntityRendererRegistry.INSTANCE.register(modelframeblockentity, ModelFrameBlockEntityRender::new);
        BlockEntityRendererRegistry.INSTANCE.register(shelfblockentity, ShelfBlockEntityRender::new);
        BlockEntityRendererRegistry.INSTANCE.register(showblockentity, ShowBlockEntityRender::new);
        BlockEntityRendererRegistry.INSTANCE.register(mixedblockentity, MixedBlockEntityRender::new);

//        FabricModelPredicateProviderRegistry.register(new Identifier("direction"), (itemStack, clientWorld, livingEntity,i) -> {
//            return itemStack.hasNbt() ? itemStack.getNbt().getInt("TransDirection") : 0;//I don't know i
//        });
        FabricModelPredicateProviderRegistry.register(Yuushya.yuushyaElements.itemsregister.get("form_trans_item"),new Identifier("direction"), (itemStack, clientWorld, livingEntity,i) -> {return itemStack.hasNbt() ? itemStack.getNbt().getInt("TransDirection") : 0;});
        FabricModelPredicateProviderRegistry.register(Yuushya.yuushyaElements.itemsregister.get("pos_trans_item"),new Identifier("direction"), (itemStack, clientWorld, livingEntity,i) -> {return itemStack.hasNbt() ? itemStack.getNbt().getInt("TransDirection") : 0;});


    }
}
