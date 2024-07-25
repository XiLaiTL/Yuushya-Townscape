package com.yuushya.neoforge;

import com.yuushya.Yuushya;
import com.yuushya.YuushyaClient;
import com.yuushya.entity.ChairEntity;
import com.yuushya.entity.ChairEntityRender;
import com.yuushya.particle.YuushyaParticleFactory;
import com.yuushya.registries.YuushyaRegistries;
import com.yuushya.registries.YuushyaRegistryConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

//@EventBusSubscriber(value = Dist.CLIENT, modid = Yuushya.MOD_ID)
@Mod(value = Yuushya.MOD_ID, dist = Dist.CLIENT)
public class YuushyaClientNeoForge {
    public YuushyaClientNeoForge(IEventBus modBus){
        if (FMLEnvironment.dist.isClient()) {
            modBus.addListener(this::onInitializeClient);
            modBus.addListener(this::onEntityRendererRegister);
            modBus.addListener(this::onParticleFactoryRegistration);
        }
    }


    public void onInitializeClient(FMLClientSetupEvent event) {
        YuushyaClient.onInitializeClient();
    }


    public void onEntityRendererRegister(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer((EntityType<ChairEntity>) YuushyaRegistries.CHAIR_ENTITY.get(), ChairEntityRender::new);
    }


    public void onParticleFactoryRegistration(RegisterParticleProvidersEvent event) {
        YuushyaRegistryConfig.YuushyaRawParticleMap.values().forEach((e)->{
            Minecraft.getInstance().particleEngine.register((ParticleType<SimpleParticleType>)YuushyaRegistries.PARTICLE_TYPES.get(e.name).get(), (spriteSet)-> YuushyaParticleFactory.create(e,spriteSet));
        });
        //Minecraft.getInstance().particleEngine.register((ParticleType<SimpleParticleType>) YuushyaRegistries.PARTICLE_TYPES.get("leaf_particle").get(), LeafParticle.Factory::new);
    }


}
/*
        for(BlockState blockState: YuushyaRegistries.BLOCKS.get("showblock").get().getStateDefinition().getPossibleStates()){
            ModelResourceLocation modelResourceLocation= BlockModelShaper.stateToModelLocation(blockState);
            BakedModel existingModel = event.getModelRegistry().get(modelResourceLocation);
            if (existingModel !=null&&!(existingModel instanceof ShowBlockModel))
                event.getModelRegistry().put(modelResourceLocation,new ShowBlockModel(existingModel));
        }
 */


