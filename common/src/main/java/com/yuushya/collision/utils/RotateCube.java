package com.yuushya.collision.utils;

import com.yuushya.collision.data.CollisionItem;
import com.yuushya.collision.data.Model;

import java.util.List;

public class RotateCube {

    private static List<Vec3> getPossiblePointList(Model.Element element){
        Vec3 from = new Vec3(element.from.get(0), element.from.get(1), element.from.get(2));
        Vec3 to = new Vec3(element.to.get(0), element.to.get(1), element.to.get(2));
        if(element.rotation==null||element.rotation.angle==0){
            return List.of(from,to);
        }
        Vec3 rotator = new Vec3(element.rotation.origin.get(0),element.rotation.origin.get(1),element.rotation.origin.get(2));
        Vec3 from_after = RotatePoint.rotate(from,rotator,element.rotation.axis,element.rotation.angle);
        Vec3 to_after = RotatePoint.rotate(to,rotator,element.rotation.axis,element.rotation.angle);
        switch(element.rotation.axis){
            case "x":{
                return List.of(
                        from_after,
                        to_after,
                        RotatePoint.rotate(new Vec3(from.x,from.y,to.z),rotator,element.rotation.axis,element.rotation.angle),
                        RotatePoint.rotate(new Vec3(to.x, to.y, from.z),rotator,element.rotation.axis,element.rotation.angle)
                        );
            }
            case "y":{
                return List.of(
                        from_after,
                        to_after,
                        RotatePoint.rotate(new Vec3(from.x,to.y,to.z),rotator,element.rotation.axis,element.rotation.angle),
                        RotatePoint.rotate(new Vec3(to.x, from.y, from.z),rotator,element.rotation.axis,element.rotation.angle)
                );
            }
            case "z":{
                return List.of(
                        from_after,
                        to_after,
                        RotatePoint.rotate(new Vec3(from.x,to.y,from.z),rotator,element.rotation.axis,element.rotation.angle),
                        RotatePoint.rotate(new Vec3(to.x, from.y, to.z),rotator,element.rotation.axis,element.rotation.angle)
                );
            }
            default:{return List.of(from_after,to_after);}
        }
    }
    public static final double MAX = 1000000000;
    public static CollisionItem.Model.Element rotate(Model.Element element){
        List<Vec3> list = getPossiblePointList(element);
        Vec3 max = new Vec3(-MAX,-MAX,-MAX);
        Vec3 min = new Vec3(MAX,MAX,MAX);
        for(var point:list){
            max.x=Math.max(max.x,point.x); max.y=Math.max(max.y,point.y); max.z=Math.max(max.z,point.z);
            min.x=Math.min(min.x,point.x); min.y=Math.min(min.y,point.y); min.z=Math.min(min.z,point.z);
        }
        return new CollisionItem.Model.Element(min.x,min.y,min.z,max.x,max.y,max.z);
    }
}
