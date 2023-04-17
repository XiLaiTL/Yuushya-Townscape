package com.yuushya.collision.utils;

import com.yuushya.collision.data.CollisionItem;
import com.yuushya.collision.data.Model;

import java.util.List;

class Element{
    Vec3 from;
    Vec3 to;

    public Element(Double from_x,Double from_y,Double from_z,Double to_x,Double to_y,Double to_z){
        from = new Vec3(from_x,from_y,from_z);
        to = new Vec3(to_x,to_y,to_z);
    }
    public Element(CollisionItem.Model.Element element){
        from = new Vec3(element.from.get(0),element.from.get(1),element.from.get(2));
        to = new Vec3(element.to.get(0),element.to.get(1),element.to.get(2));
    }

    public Element rotate(Integer x, Integer y){
        if(x!=null){
            switch (x){
                case 270: set(this.from.x,this.from.z,-this.from.y,this.to.x,this.to.z,-this.to.y); break;
                case 180: set(this.from.x,-this.from.y,-this.from.z,this.to.x,-this.to.y,-this.to.z);break;
                case 90:  set(this.from.x,-this.from.z,this.from.y,this.to.x,-this.to.z,this.to.y);break;
                case 0:   set(this.from.x,this.from.y,this.from.z,this.to.x,this.to.y,this.to.z);break;
            }
        }
        if(y!=null){
            switch (y){
                case 0:   set(this.from.x,this.from.y,this.from.z,this.to.x,this.to.y,this.to.z);break;
                case 90:  set(-this.from.z,this.from.y,this.from.x,-this.to.z,this.to.y,this.to.x);break;
                case 180: set(-this.from.x,this.from.y,-this.from.z,-this.to.x,this.to.y,-this.to.z);break;
                case 270: set(this.from.z,this.from.y,-this.from.x,this.to.z,this.to.y,-this.to.x);break;
            }
        }
        return this;
    }
    public Element move(Double offset){
        from.x+=offset; from.y+=offset; from.z+=offset;
        to.x+=offset; to.y+=offset; to.z+=offset;
        return this;
    }
    public Element scale(Double offset){
        from.x*=offset; from.y*=offset; from.z*=offset;
        to.x*=offset; to.y*=offset; to.z*=offset;
        return this;
    }

    public Element set(Double from_x,Double from_y,Double from_z,Double to_x,Double to_y,Double to_z){
        from = new Vec3(from_x,from_y,from_z);
        to = new Vec3(to_x,to_y,to_z);
        return this;
    }



    public CollisionItem.Model.Element toElement(){
        return  new CollisionItem.Model.Element(Math.min(from.x, to.x),Math.min(from.y,to.y),Math.min(from.z, to.z),Math.max(from.x, to.x),Math.max(from.y,to.y),Math.max(from.z, to.z));
    }
}

public class RotateModel {

    public static CollisionItem.Model.Element rotate(CollisionItem.Model.Element element, Integer x, Integer y){
        return new Element(element)
                .move(-8.0) //旋转前移动到中心
                .rotate(x,y)
                .move(8.0).toElement();
    }

    public static List<CollisionItem.Model.Element> rotate(Model model,Integer x,Integer y){
        return model.elements.stream().map(el->
                RotateModel.rotate(
                        RotateCube.rotate(el)
                        ,x,y))
                .toList();
    }


}

