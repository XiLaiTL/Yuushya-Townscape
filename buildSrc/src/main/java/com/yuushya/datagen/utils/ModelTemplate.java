package com.yuushya.datagen.utils;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModelTemplate {
    private final Optional<ResourceLocation> model;
    private final Set<TextureSlot> requiredSlots;
    private final Optional<String> suffix;

    public ModelTemplate(Optional<ResourceLocation> optional, Optional<String> optional2, TextureSlot... textureSlots) {
        this.model = optional;
        this.suffix = optional2;
        this.requiredSlots = Arrays.stream(textureSlots).collect(Collectors.toSet());
    }

    public ResourceLocation create(ResourceLocation resourceLocation, TextureMapping textureMapping, BiConsumer<ResourceLocation, Supplier<JsonElement>> biConsumer) {
        Map<TextureSlot, ResourceLocation> map = this.createMap(textureMapping);
        biConsumer.accept(resourceLocation, () -> {
            JsonObject jsonObject = new JsonObject();
            this.model.ifPresent(resourceLocation1 -> jsonObject.addProperty("parent", resourceLocation1.toString()));
            if (!map.isEmpty()) {
                JsonObject jsonObject2 = new JsonObject();
                map.forEach((textureSlot, resourceLocation2) -> jsonObject2.addProperty(textureSlot.getId(), resourceLocation2.toString()));
                jsonObject.add("textures", jsonObject2);
            }
            return jsonObject;
        });
        return resourceLocation;
    }

    private Map<TextureSlot, ResourceLocation> createMap(TextureMapping textureMapping) {
        return Stream.concat(this.requiredSlots.stream(), textureMapping.getForced()).collect(Collectors.toMap(i->i,i->textureMapping.get(i)));
    }

    private static ModelTemplate createItem(String string, TextureSlot... textureSlots) {
        return new ModelTemplate(Optional.of(new ResourceLocation("minecraft", "item/" + string)), Optional.empty(), textureSlots);
    }

    private static ModelTemplate create(String string, TextureSlot ... textureSlots) {
        return new ModelTemplate(Optional.of(new ResourceLocation("minecraft", "block/" + string)), Optional.empty(), textureSlots);
    }

    public static final ModelTemplate FLAT_ITEM = ModelTemplate.createItem("generated", TextureSlot.LAYER0);
    public static final ModelTemplate CUBE_ALL = ModelTemplate.create("cube_all", TextureSlot.ALL);

}