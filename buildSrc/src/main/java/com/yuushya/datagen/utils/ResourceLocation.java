package com.yuushya.datagen.utils;

import java.util.Objects;

public record ResourceLocation(String namespace, String path) {
    public static ResourceLocation parse (String single) {
        String[] split = single.split(":");
        if (split.length == 1) {
            return new ResourceLocation("minecraft",split[0]);
        } else {
            return new ResourceLocation(split[0],split[1]);
        }
    }
    public static ResourceLocation parse(String namespace,String path){
        return new ResourceLocation(namespace,path);
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

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof ResourceLocation) ) return false;
        return this.toString().equals(o.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, path);
    }
}
