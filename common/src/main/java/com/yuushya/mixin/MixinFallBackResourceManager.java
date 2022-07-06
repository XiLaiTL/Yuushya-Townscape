package com.yuushya.mixin;

import com.yuushya.datagen.YuushyaDataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.FallbackResourceManager;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.SimpleResource;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/*
---ResourceManager类中的getResource方法，是用来加载textures，models，particles和shaders相关资源的。
---ResourceManager类中的getAllResource方法，则是用来加载font，sound，lang和blockstates相关资源的。
---要注意的是，第一个方法时client启动时调用的，第二个方法时server启动时调用的。
* */
@Mixin(FallbackResourceManager.class)
public class MixinFallBackResourceManager {
    @Inject(
            method = "getResources",
            at=@At(
                    value ="INVOKE",
                    target = "Lnet/minecraft/server/packs/resources/FallbackResourceManager;" +
                            "validateLocation(Lnet/minecraft/resources/ResourceLocation;)" +
                            "V",
                    shift = At.Shift.AFTER
            ),
            cancellable = true
    )
    public void getResources(ResourceLocation id, CallbackInfoReturnable<List<Resource>> cir){
        YuushyaDataProvider blockstateProvider = YuushyaDataProvider.of(YuushyaDataProvider.DataType.BlockState);
        if (!id.toString().contains("blockstates") || !blockstateProvider.contain(id)) return;
        cir.setReturnValue(List.of(
                new SimpleResource(id.getNamespace(), id, new ByteArrayInputStream(blockstateProvider.get(id).toString().getBytes(StandardCharsets.UTF_8)), null))
        );
        cir.cancel();
    }

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
    public void getResource(@NotNull ResourceLocation id, CallbackInfoReturnable<Resource> cir){
        YuushyaDataProvider yuushyaDataProvider=YuushyaDataProvider.of(id);
        if (id.toString().contains("blockstates")) return;
        if (yuushyaDataProvider.type(YuushyaDataProvider.DataType.LootTable).contain(id)){
            cir.cancel();return;
        }
        if (yuushyaDataProvider.type(YuushyaDataProvider.DataType.ItemModel).contain(id)
                ||yuushyaDataProvider.type(YuushyaDataProvider.DataType.BlockModel).contain(id)){
            Resource resource = new SimpleResource(id.getNamespace(), id, new ByteArrayInputStream(yuushyaDataProvider.get(id).toString().getBytes(StandardCharsets.UTF_8)), null);
            cir.setReturnValue(resource);
            cir.cancel();return;
        }
    }
}