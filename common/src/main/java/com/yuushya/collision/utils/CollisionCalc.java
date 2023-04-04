package com.yuushya.collision.utils;

import com.yuushya.collision.data.Model;

import java.util.List;

public class CollisionCalc {

    private static List<Vec3> getPossiblePointList(Model.Element element){
        Vec3 rotator = new Vec3(element.rotation.origin.get(0),element.rotation.origin.get(1),element.rotation.origin.get(2));
        Vec3 from = new Vec3(element.from.get(0), element.from.get(1), element.from.get(2));
        Vec3 to = new Vec3(element.to.get(0), element.to.get(1), element.to.get(2));
        return List.of(
            RotatePoint.rotate(from,rotator,element.rotation.axis,element.rotation.angle),
            RotatePoint.rotate(to,rotator,element.rotation.axis,element.rotation.angle),
            RotatePoint.rotate(new Vec3(to.x, from.y, from.z),rotator,element.rotation.axis,element.rotation.angle),
            RotatePoint.rotate(new Vec3(from.x,to.y,to.z),rotator,element.rotation.axis,element.rotation.angle)
        );
    }
    public static final double MAX = 1000000000;
    public static List<Double> getPointList(Model.Element element){
        List<Vec3> list = getPossiblePointList(element);
        Vec3 max = new Vec3(-MAX,-MAX,-MAX);
        Vec3 min = new Vec3(MAX,MAX,MAX);
        for(var point:list){
            max.x=Math.max(max.x,point.x); max.y=Math.max(max.y,point.y); max.z=Math.max(max.z,point.z);
            min.x=Math.min(min.x,point.x); min.y=Math.min(min.y,point.y); min.z=Math.min(min.z,point.z);
        }
        return List.of(max.x,max.y,max.z,min.x,min.y,min.z);
    }
}
