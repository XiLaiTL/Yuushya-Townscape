package net.cocofish.yuushya.forge;

import dev.architectury.platform.forge.EventBuses;
import net.cocofish.yuushya.Yuushya;
import net.cocofish.yuushya.YuushyaClient;
import net.cocofish.yuushya.registries.YuushyaRegistries;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Yuushya.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class YuushyaClientForge {
    @SubscribeEvent
    public static void onInitializeClient(FMLClientSetupEvent event) {
        //EventBuses.registerModEventBus(Yuushya.MOD_ID, (IEventBus) event);
        YuushyaClient.onInitializeClient();
    }
    @SubscribeEvent
    public static void onModelBaked(ModelBakeEvent event){
        for(BlockState blockState: YuushyaRegistries.BLOCKS.get("showblock").get().getStateDefinition().getPossibleStates()){
            ModelResourceLocation modelResourceLocation= BlockModelShaper.stateToModelLocation(blockState);
            BakedModel existingModel = event.getModelRegistry().get(modelResourceLocation);
            if (existingModel !=null&&!(existingModel instanceof ShowBlockModel))
                event.getModelRegistry().put(modelResourceLocation,new ShowBlockModel(existingModel));
        }
    }
}
