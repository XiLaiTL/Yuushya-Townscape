package com.yuushya.collision.data;

import java.util.List;

public class CollisionItem {
    public String namespace;
    public String id;
    public String parent;
    public List<Model> blockstates;
    public static class Model{
        public String variant;
        public Integer x;
        public Integer y;
        public List<Element> collision;
        public static class Element{
            public List<Double> from;
            public List<Double> to;
            public Element(Double from_x,Double from_y,Double from_z,Double to_x,Double to_y,Double to_z){
                this.from=List.of(from_x,from_y,from_z);
                this.to=List.of(to_x,to_y,to_z);
            }
        }
    }
}