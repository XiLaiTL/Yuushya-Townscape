package com.yuushya.datagen.utils;

import net.minecraft.resources.ResourceLocation;

public class ResourceLocationConvert {
    public static ResourceLocation from(com.yuushya.datagen.utils.ResourceLocation resourceLocation){
        return new ResourceLocation(resourceLocation.getNamespace(),resourceLocation.getPath());
    }
    public static com.yuushya.datagen.utils.ResourceLocation from(net.minecraft.resources.ResourceLocation resourceLocation){
        return new com.yuushya.datagen.utils.ResourceLocation(resourceLocation.getNamespace(),resourceLocation.getPath());
    }
}
