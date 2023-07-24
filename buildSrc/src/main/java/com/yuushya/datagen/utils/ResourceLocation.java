package com.yuushya.datagen.utils;


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

}
