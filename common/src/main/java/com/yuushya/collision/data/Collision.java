package com.yuushya.collision.data;


import java.util.List;

public class Collision {
    public List<Item> items;
    public static class Item{
        public String namespace;
        public String id;
        public List<Model> blockstates;
        public static class Model{
            public String variant;
            public Integer x;
            public Integer y;
            public List<List<Double>> collision;
        }
    }
}
