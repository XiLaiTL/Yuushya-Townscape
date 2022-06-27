package com.yuushya.mixin;

import com.google.common.collect.Lists;
import com.yuushya.datagen.BuilderPool;
import com.yuushya.datagen.ModelBuilder;
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
import java.util.List;
import java.util.Map;

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
        Map<String, ModelBuilder> variantBuilderPool = BuilderPool.variantBuilderPool;
        //只加载blockstates，只加载map中有相同id的资源
        if (!id.toString().contains("blockstates")
                || !variantBuilderPool.containsKey(id.toString())) {
            return;
        }
        List<Resource> list = Lists.newArrayList();
        //由于是个list，所以还有其他资源可以加载，不过我没有细研究了，有兴趣的可以跟踪下
        list.add(new SimpleResource(id.getNamespace(), id, new ByteArrayInputStream(variantBuilderPool.get(id.toString()).genJson().getBytes(StandardCharsets.UTF_8)), null));
        cir.setReturnValue(list);
        cir.cancel();
    }
}
