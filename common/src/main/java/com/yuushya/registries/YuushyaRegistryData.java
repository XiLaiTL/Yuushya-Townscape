package com.yuushya.registries;

import java.util.ArrayList;
import java.util.List;

public class YuushyaRegistryData{
    List<Block> block = new ArrayList<>();
    List<Item> item = new ArrayList<>();
    List<Particle> particle = new ArrayList<>();
    String version;

    public static class Block{
        String classtype;
        String rendertype;
        String name;
        String itemgroup;
        Properties properties;
        ColorTint colortint;
        public static class Properties {
            int hardness;
            int resistance;
            int lightlevel;
            float ambientocclusionlightlevel;
            String sound;
            String material;
            boolean issolid;
            boolean havecollision;
            boolean isdelicate;
            boolean ishub;
            int lines;
        }
        public static class ColorTint{
            String colortype;
            String colorstring;
        }
    }
    public static class Item{
        String classtype;
        String name;
        String itemgroup;
        public static class Properties {
            int maxcout;
            int maxdamage;
            String rarity;
            String equipment;
            boolean fireproof;
            int lines;
            String createnbt;
            String cancelnbt;
        }
    }
    public static class Particle{}
    
}
