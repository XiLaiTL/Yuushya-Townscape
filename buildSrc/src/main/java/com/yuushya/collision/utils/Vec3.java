package com.yuushya.collision.utils;

public class Vec3{
    public double x,y,z;
    public Vec3(double x,double y,double z){this.x=x;this.y=y;this.z=z;}
    @Override
    public String toString(){return ""+this.x+" "+this.y+" "+this.z;}
}
class Vec2{
    public double x,y;
    public Vec2(double x,double y){ this.x =x;this.y=y; }
    @Override
    public String toString(){return ""+this.x+" "+this.y;}

}
