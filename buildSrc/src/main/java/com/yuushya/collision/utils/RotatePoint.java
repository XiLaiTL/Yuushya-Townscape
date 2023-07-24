package com.yuushya.collision.utils;

public class RotatePoint {

    public static Vec2 rotate(Vec2 target,Vec2 origin,double angle_r){
        return new Coordinate(origin)
                .new Point(target).rotate(angle_r).toXY();
    }
    public static Vec3 rotate(Vec3 target,Vec3 rotator,String axis,double angle_d){
        if (angle_d == 0) return target;
        double angle = Math.toRadians(angle_d);
        return switch (axis){
            case "x" ->{
                Vec2 res2d = rotate(new Vec2(target.y,target.z), new Vec2(rotator.y,rotator.z),angle);
                yield new Vec3(target.x,res2d.x,res2d.y);
            }
            case "y" -> {
                Vec2 res2d = rotate(new Vec2(target.z,target.x),new Vec2(rotator.z,rotator.x), angle);
                yield new Vec3(res2d.y,target.y,res2d.x);
            }
            case "z" -> {
                Vec2 res2d = rotate(new Vec2(target.x,target.y),new Vec2(rotator.x,rotator.y), angle);
                yield new Vec3(res2d.x,res2d.y,target.z);
            }
            default -> target;
        };
    }
}

class Coordinate{
    private final Vec2 _origin;

    public Coordinate(Vec2 origin){this._origin =origin;}

    public class Point{
        private double _radius;
        private double _angle;

        public Point(Vec2 target){
            double width = target.x- Coordinate.this._origin.x;
            double height = target.y-Coordinate.this._origin.y;
            this._radius = Math.sqrt(Math.pow(width, 2)+ Math.pow(height,2));
            this._angle = Math.atan2(height,width);
        }

        public Point rotate(double angle){
            this._angle +=angle;
            return this;
        }

        public Vec2 toXY(){
            return new Vec2(
                    Coordinate.this._origin.x+this._radius *Math.cos(this._angle),
                    Coordinate.this._origin.y+this._radius *Math.sin(this._angle));
        }

    }


}