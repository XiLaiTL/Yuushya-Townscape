package com.yuushya.datagen.utils;

import com.google.gson.*;
import net.minecraft.util.GsonHelper;

import java.lang.reflect.Type;

public final class ResourceLocation {
    private final String namespace;
    private final String path;

    public ResourceLocation(String namespace, String path) {
        this.namespace = namespace;
        this.path = path;
    }

    public ResourceLocation(String single) {
        String[] split = single.split(":");
        if (split.length == 1) {
            this.namespace= "minecraft";
            this.path = split[0];
        } else {
            this.namespace = split[0];
            this.path = split[1];
        }
    }

    public String toString() {
        return this.namespace + ":" + this.path;
    }

    public String getNamespace() {
        return namespace;
    }
    public String getPath() {
        return path;
    }
    public String toRelativePath(String resourceType){
        return this.namespace+"/"+resourceType+"/"+this.path;
    }


    public static class Serializer
            implements JsonDeserializer<ResourceLocation>,
            JsonSerializer<ResourceLocation> {
        @Override
        public ResourceLocation deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return new ResourceLocation(GsonHelper.convertToString(jsonElement, "location"));
        }

        @Override
        public JsonElement serialize(ResourceLocation resourceLocation, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(resourceLocation.toString());
        }
    }
}
