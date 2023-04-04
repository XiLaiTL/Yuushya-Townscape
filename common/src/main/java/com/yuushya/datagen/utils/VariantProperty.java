package com.yuushya.datagen.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.function.Function;

public class VariantProperty<T> {

    String key;
    Function<T, JsonElement> serializer;

    VariantProperty(String string, Function<T, JsonElement> function) {
        this.key = string;
        this.serializer = function;
    }

    public VariantProperty<T>.Value withValue(T object) {
        return new VariantProperty<T>.Value(object);
    }

    public String toString() {
        return this.key;
    }

    public class Value {
        private final T value;

        public Value(T object) {
            this.value = object;
        }

        public VariantProperty<T> getKey() {
            return VariantProperty.this;
        }

        public void addToVariant(JsonObject jsonObject) {
            jsonObject.add(VariantProperty.this.key, VariantProperty.this.serializer.apply(this.value));
        }

        public String toString() {
            return VariantProperty.this.key + "=" + this.value;
        }
    }

    public static enum Rotation {
        R0(0),
        R90(90),
        R180(180),
        R270(270);

        final int value;

        private Rotation(int j) {
            this.value = j;
        }
    }

    public static final VariantProperty<Rotation> X_ROT = new VariantProperty<Rotation>("x", rotation -> new JsonPrimitive(rotation.value));
    public static final VariantProperty<Rotation> Y_ROT = new VariantProperty<Rotation>("y", rotation -> new JsonPrimitive(rotation.value));
    public static final VariantProperty<ResourceLocation> MODEL = new VariantProperty<ResourceLocation>("model", resourceLocation -> new JsonPrimitive(resourceLocation.toString()));
    public static final VariantProperty<Boolean> UV_LOCK = new VariantProperty<Boolean>("uvlock", JsonPrimitive::new);
    public static final VariantProperty<Integer> WEIGHT = new VariantProperty<Integer>("weight", JsonPrimitive::new);

}

