package com.yuushya.collision.data;

import java.util.List;

public class Model {
    public String parent;
    public List<Element> elements;
    public static class Element{
        public List<Float> from;
        public List<Float> to;
        public Rotation rotation;
        public static class Rotation{
            public Float angle;
            public String axis;
            public List<Float> origin;
        }
    }
}
