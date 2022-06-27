package com.yuushya.forge;

import com.yuushya.Yuushya;
import com.yuushya.registries.YuushyaRegistries;
import com.yuushya.YuushyaClient;
import com.yuushya.forge.client.ShowBlockModel;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Yuushya.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class YuushyaClientForge {
    @SubscribeEvent
    public static void onInitializeClient(FMLClientSetupEvent event) {
        YuushyaClient.onInitializeClient();
    }
    @SubscribeEvent
    public static void onModelBaked(ModelBakeEvent event){
        event.getModelRegistry().put(BlockModelShaper.stateToModelLocation(YuushyaRegistries.BLOCKS.get("showblock").get().defaultBlockState()),new ShowBlockModel());
    }



    /**
     * getColor是对面片执行的，所以只需要知道这个面片事实上来自哪个方块就能知道颜色
     * 而且原版方块的tintIndex的值除了-1之外似乎设为多少都无所谓
     * 那么可以在生成面片时存储其来自的blockState
     * Block类刚好可以将方块状态和id互相转换
     * 前24位为原方块的blockState，后8位为原方块的tint（若其为正）
     */
    @SubscribeEvent
    public static void handleBlockColor(ColorHandlerEvent.Block event) {
        event.getBlockColors().register(
                (state, view, pos, tintIndex) -> {
                    if (tintIndex > -1) {
                        // decodeTintWithState
                        // 假设原tint为负数，则最高位为1，通常可以返回空气（因为不太可能出现上千万的方块状态），那么空气也不会被染色
                        BlockState trueState = Block.stateById(tintIndex >> 8);
                        int trueTint = tintIndex & 0xFF;
                        return event.getBlockColors().getColor(trueState, view, pos, trueTint);
                    } else {
                        return 0xFFFFFFFF;
                    }
                },
                YuushyaRegistries.SHOW_BLOCK.get()
        );
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


