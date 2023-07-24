package com.yuushya.collision.data;

import java.util.ArrayList;
import java.util.List;

public class CollisionItem {

    public List<String> children;
    public List<Model> blockstates;
    public static class Model{
        public String variant;
        public List<Element> collision;
        public static class Element{
            public List<Double> from;
            public List<Double> to;
            public Element(Double from_x,Double from_y,Double from_z,Double to_x,Double to_y,Double to_z){
                this.from= new ArrayList<>();
                this.from.add(from_x);
                this.from.add(from_y);
                this.from.add(from_z);
                this.to= new ArrayList<>();
                this.to.add(to_x);
                this.to.add(to_y);
                this.to.add(to_z);
            }
        }
    }
}