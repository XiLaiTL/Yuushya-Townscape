package com.yuushya.datagen.data;

public class PackData {
    public Pack pack;
    public Mod mod;
    public static class Pack{
        public String description;
    }
    public static class Mod{
        public String version;
        public String name;
    }
    public void checkVaild(String defaultName){
        if(this.pack ==null) this.pack = new Pack();
        if(this.pack.description == null) this.pack.description = "Auto Generation By Yuushya Townscape";
        if(this.mod ==null) this.mod =new Mod();
        if(this.mod.version ==null) this.mod.version = "0.0.1";
        if(this.mod.name == null) this.mod.name = defaultName;
    }
}
