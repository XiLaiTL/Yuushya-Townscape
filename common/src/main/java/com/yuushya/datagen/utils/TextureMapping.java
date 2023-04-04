package com.yuushya.datagen.utils;



import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class TextureMapping {
    private final Map<TextureSlot, ResourceLocation> slots = new HashMap<>();
    private final Set<TextureSlot> forcedSlots = new HashSet<>();

    public TextureMapping put(TextureSlot textureSlot, ResourceLocation resourceLocation) {
        this.slots.put(textureSlot, resourceLocation);
        return this;
    }

    public TextureMapping putForced(TextureSlot textureSlot, ResourceLocation resourceLocation) {
        this.slots.put(textureSlot, resourceLocation);
        this.forcedSlots.add(textureSlot);
        return this;
    }

    public Stream<TextureSlot> getForced() {
        return this.forcedSlots.stream();
    }

    public TextureMapping copySlot(TextureSlot textureSlot, TextureSlot textureSlot2) {
        this.slots.put(textureSlot2, this.slots.get(textureSlot));
        return this;
    }

    public TextureMapping copyForced(TextureSlot textureSlot, TextureSlot textureSlot2) {
        this.slots.put(textureSlot2, this.slots.get(textureSlot));
        this.forcedSlots.add(textureSlot2);
        return this;
    }

    public ResourceLocation get(TextureSlot textureSlot) {
        for (TextureSlot textureSlot2 = textureSlot; textureSlot2 != null; textureSlot2 = textureSlot2.getParent()) {
            ResourceLocation resourceLocation = this.slots.get(textureSlot2);
            if (resourceLocation == null) continue;
            return resourceLocation;
        }
        throw new IllegalStateException("Can't find texture for slot " + textureSlot);
    }

    public TextureMapping copyAndUpdate(TextureSlot textureSlot, ResourceLocation resourceLocation) {
        TextureMapping textureMapping = new TextureMapping();
        textureMapping.slots.putAll(this.slots);
        textureMapping.forcedSlots.addAll(this.forcedSlots);
        textureMapping.put(textureSlot, resourceLocation);
        return textureMapping;
    }

    public static TextureMapping cube(ResourceLocation resourceLocation) {
        return new TextureMapping().put(TextureSlot.ALL, resourceLocation);
    }
    public static TextureMapping layer0(ResourceLocation resourceLocation) {
        return new TextureMapping().put(TextureSlot.LAYER0, resourceLocation);
    }
}
