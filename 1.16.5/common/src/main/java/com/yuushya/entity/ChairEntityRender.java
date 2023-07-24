package com.yuushya.entity;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.resources.ResourceLocation;

public class ChairEntityRender extends EntityRenderer<ChairEntity> {

    public ChairEntityRender(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher);
    }

    @Override
    public ResourceLocation getTextureLocation(ChairEntity entity) { return null;}
}
