package com.yuushya.datagen.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Utils {
    public static final Splitter COMMA_SPLITTER = Splitter.on(",");
    public static final String MOD_ID = "yuushya";
    public static <T> List<List<T>> CartesianProduct(List<List<T>> fatherCollection){
        int size=1; for (List<T> list:fatherCollection){size*=list.size();}
        List<List<T>> res=new ArrayList<>(size);
        for(int i=0;i<size;i++){int j=i;
            List<T> childCollection =new ArrayList<>(fatherCollection.size());
            for (List<T> list:fatherCollection){
                childCollection.add(list.get(j % list.size()));
                j/=list.size();
            }
            res.add(childCollection);
        }
        return res;
    }

    public static class Splitter {
        private String seperator = "";
        Splitter(String seperator){
            this.seperator = seperator;
        }
        public static Splitter on(String seperator){
            return new Splitter(seperator);
        }
        public Iterable<String> split(String sentence){
            return Arrays.stream(sentence.split(seperator)).toList();
        }
    }

}
