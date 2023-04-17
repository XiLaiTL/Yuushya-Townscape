package com.yuushya.collision.utils;

import com.yuushya.collision.data.CollisionItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OptimizeModel {
    public static double distance2(CollisionItem.Model.Element el1, CollisionItem.Model.Element el2){
        double res=0;
        for(int i=0;i<3;i++){
            double from1_to2 = el2.to.get(i)-el1.from.get(i);
            double to1_from2 = el1.to.get(i)-el2.from.get(i);
            double dis=0.0;//如果异号，距离为0
            if(from1_to2*to1_from2>0){
                dis=Math.min(Math.abs(from1_to2),Math.abs(to1_from2));
            }
            res+=dis*dis;
        }
        return res;
    }

    public static boolean include(CollisionItem.Model.Element el1,CollisionItem.Model.Element el2){
        //或者距离为零
        boolean res=true;
        for(int i=0;i<3;i++){
            res = res && el1.from.get(i)<=el2.from.get(i);
            res = res && el1.to.get(i)>=el2.to.get(i);
        }
        return res;
    }
    public static CollisionItem.Model.Element combine(CollisionItem.Model.Element el1,CollisionItem.Model.Element el2){
        return new CollisionItem.Model.Element(
                Math.min(el1.from.get(0),el2.from.get(0)),Math.min(el1.from.get(1),el2.from.get(1)),Math.min(el1.from.get(2),el2.from.get(2)),
                Math.max(el1.to.get(0),el2.to.get(0)),Math.max(el1.to.get(1),el2.to.get(1)),Math.max(el1.to.get(2),el2.to.get(2))
        );
    }

    public static double volume(CollisionItem.Model.Element element){
        double res=1;
        for(int i=0;i<3;i++){
            res*=(element.to.get(i)-element.from.get(i));
        }
        return res;
    }

    private static class ElementRelation{
        public CollisionItem.Model.Element element;
        public Map<ElementRelation,Double> distance = new HashMap<>();
        public boolean isInclude = false;
        public double volume = 16*16*16;
        public ElementRelation(CollisionItem.Model.Element element){
            this.element = element;
            this.volume = volume(element);
        }

    }

    private static final Double MAX_VOLUME = 16*16*12.0;
    public static List<CollisionItem.Model.Element> optimize(List<CollisionItem.Model.Element> elements, int controlNum){
        Map<Boolean,List<ElementRelation>> filterMap = elements.stream()
                .map(ElementRelation::new)
                .filter(x->x.volume>0) //抛弃=0的
                .collect(Collectors.groupingBy(x->x.volume>MAX_VOLUME));
        List<CollisionItem.Model.Element> res = new ArrayList<>();
        if(filterMap.containsKey(true)) res.addAll(filterMap.get(true).stream().map(x -> x.element).toList());
        if(filterMap.containsKey(false)){
            List<ElementRelation> relation = filterMap.get(false);
            int groupNum = relation.size()/controlNum;
            groupNum = groupNum > 0 ? groupNum : 1;
            for(int i=0;i<relation.size();i++){
                ElementRelation first = relation.get(i);
                if(!first.isInclude){
                    for(int j=i+1;j< relation.size();j++){
                        ElementRelation second = relation.get(j);
                        if(!second.isInclude){
                            double distance = distance2(first.element,second.element);
                            first.distance.put(second,distance);
                            second.distance.put(first,distance);
                        }
                    }
                    int groupNumTime = groupNum;
                    List<ElementRelation> sortByValue = first.distance.entrySet().stream()
                            .sorted(Map.Entry.comparingByValue()).map(Map.Entry::getKey).toList();
                    for(ElementRelation current : sortByValue){
                        if(!current.isInclude && groupNumTime>0){
                            first.element = combine(first.element,current.element);
                            groupNumTime--;
                            current.isInclude = true;
                        }
                    }
                    res.add(first.element);
                }
            }
        }
        return res;
    }
}
