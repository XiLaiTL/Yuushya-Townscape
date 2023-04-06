package com.yuushya.forge.mixin;

import com.yuushya.Yuushya;
import com.yuushya.datagen.YuushyaDataProvider;
import com.yuushya.datagen.utils.ResourceLocationConvert;
import com.yuushya.utils.YuushyaLogger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.FallbackResourceManager;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.SimpleResource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@Mixin(FallbackResourceManager.class)
public class MixinFallBackResourceManager {
    @Inject(
            method = "getResource",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/packs/resources/FallbackResourceManager;" +
                            "validateLocation(Lnet/minecraft/resources/ResourceLocation;)" +
                            "V",
                    shift = At.Shift.AFTER
            ),
            cancellable = true
    )
    public void getResource(ResourceLocation id, CallbackInfoReturnable<Resource> cir){
        YuushyaDataProvider yuushyaDataProvider=YuushyaDataProvider.of(ResourceLocationConvert.from(id));
        if (id.toString().contains("blockstates")) return;
        if (yuushyaDataProvider.type(YuushyaDataProvider.DataType.LootTable).contain(ResourceLocationConvert.from(id))){
            cir.cancel();return;
        }
        if (yuushyaDataProvider.type(YuushyaDataProvider.DataType.ItemModel).contain(ResourceLocationConvert.from(id))
                ||yuushyaDataProvider.type(YuushyaDataProvider.DataType.BlockModel).contain(ResourceLocationConvert.from(id))
                ||yuushyaDataProvider.type(YuushyaDataProvider.DataType.Particle).contain(ResourceLocationConvert.from(id))){
            Resource resource = new SimpleResource(id.getNamespace(), id, new ByteArrayInputStream(yuushyaDataProvider.get(ResourceLocationConvert.from(id)).toString().getBytes(StandardCharsets.UTF_8)), null);
            cir.setReturnValue(resource);
            cir.cancel();
        }
    }
}