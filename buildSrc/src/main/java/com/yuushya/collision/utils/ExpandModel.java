package com.yuushya.collision.utils;

import com.yuushya.collision.data.CollisionItem;
import com.yuushya.collision.data.Model;

import java.util.List;

public class ExpandModel {
    public static List<CollisionItem.Model.Element> expandFace(List<CollisionItem.Model.Element> elements,int width){
        double offset = width/2.0;
        return elements.stream().map(element->{
            Vec3 from = new Vec3(element.from.get(0), element.from.get(1), element.from.get(2));
            Vec3 to = new Vec3(element.to.get(0), element.to.get(1), element.to.get(2));
            if(from.x==to.x) from.x-=offset; to.x+=offset;
            if(from.y==to.y) from.y-=offset; to.y+=offset;
            if(from.z==to.z) from.z-=offset; to.z+=offset;
            return new CollisionItem.Model.Element(from.x,from.y,from.z,to.x,to.y,to.z);
        }).toList();
    }
}
