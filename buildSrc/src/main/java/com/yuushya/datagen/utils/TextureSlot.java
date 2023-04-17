package com.yuushya.datagen.utils;

import org.jetbrains.annotations.Nullable;

public class TextureSlot {
    private final String id;
    @Nullable
    private final TextureSlot parent;

    public static TextureSlot create(String string) {
        return new TextureSlot(string, null);
    }

    public static TextureSlot create(String string, TextureSlot textureSlot) {
        return new TextureSlot(string, textureSlot);
    }

    public TextureSlot(String string, @Nullable TextureSlot textureSlot) {
        this.id = string;
        this.parent = textureSlot;
    }

    public String getId() {
        return this.id;
    }

    @Nullable
    public TextureSlot getParent() {
        return this.parent;
    }

    public String toString() {
        return "#" + this.id;
    }

    public static final TextureSlot ALL = TextureSlot.create("all");
    public static final TextureSlot LAYER0 = TextureSlot.create("layer0");

}
