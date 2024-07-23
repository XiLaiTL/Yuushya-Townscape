package com.yuushya;

import com.yuushya.collision.CollisionFileReader;
import com.yuushya.registries.YuushyaRegistries;
import com.yuushya.utils.YuushyaLogger;
import dev.architectury.event.CompoundEventResult;
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.network.chat.Component;

public class YuushyaClient {
    private static boolean openOnce =true;
    public static void onInitializeClient(){
//        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES,new YuushyaResourceReloadListener());

        YuushyaRegistries.registerClient();

        ClientLifecycleEvent.CLIENT_STARTED.register((client)->{ //client render thread
            CollisionFileReader.readAllCollision();
        });

        /*
        ClientGuiEvent.SET_SCREEN.register(screen -> {
            if(openOnce && screen instanceof SelectWorldScreen) {
                openOnce = false;
                return CompoundEventResult.interruptTrue(
                        new ConfirmScreen((b)-> {
                            Minecraft.getInstance().setScreen(screen);}, Component.translatable("menu.yuushya.confirm.title"),Component.translatable("menu.yuushya.confirm.description"))
                );
            } else{
                return CompoundEventResult.pass();
            }
        });
         */
//        ClientTickEvent.CLIENT_POST.register((client)->{
//            if (client.level != null&&client.player!=null&&client.hitResult!=null &&client.hitResult.getType() == HitResult.Type.BLOCK) {
//                BlockHitResult blockHitResult = (BlockHitResult) client.hitResult;
//                BlockPos blockPos = blockHitResult.getBlockPos();
//                BlockState blockState = client.level.getBlockState(blockPos);
//                ItemStack offhandItem = client.player.getOffhandItem();
//                ItemStack mainhandItem = client.player.getMainHandItem();
//                if(offhandItem.getItem() instanceof AbstractMultiPurposeToolItem||mainhandItem.getItem() instanceof  AbstractMultiPurposeToolItem){
//                    if(blockState.getBlock() instanceof ShowBlock){
//                        ShowBlockEntity blockEntity = (ShowBlockEntity) client.level.getBlockEntity(blockPos);
//                        if(blockEntity!=null) blockEntity.increaseShowAixs();
//                    }
//                }
//            }
//        });


    }


}
