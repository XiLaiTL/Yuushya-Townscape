package com.yuushya.mixin;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.yuushya.datagen.YuushyaDataProvider;
import com.yuushya.datagen.utils.ResourceLocationConvert;
import com.yuushya.utils.YuushyaLogger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.storage.loot.LootTables;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(LootTables.class)
public class MixinLootTables {
    @Inject(
            method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V",
            at = @At("HEAD"))
    public void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler, CallbackInfo ci){

        YuushyaDataProvider.of(YuushyaDataProvider.DataType.LootTable).forEach((key,value)->{
            YuushyaLogger.info(value.get());
            map.put(ResourceLocationConvert.from(YuushyaDataProvider.toLootTableResourceLocation(key)),value.get());
            throw new RuntimeException();
        });
    }
}