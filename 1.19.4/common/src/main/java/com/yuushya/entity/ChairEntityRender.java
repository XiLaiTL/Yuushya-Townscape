package com.yuushya.entity;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class ChairEntityRender extends EntityRenderer<ChairEntity> {
    public ChairEntityRender(EntityRendererProvider.Context context) {super(context);}

    @Override
    public ResourceLocation getTextureLocation(ChairEntity entity) { return null;}
}
