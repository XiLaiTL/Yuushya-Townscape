package com.yuushya.ui;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class Mode {
    public static List<String> proposalCollision = null;
    public static List<String> imageSize = null;
    private static final Map<String, List<String>> TABLE_MAP=new HashMap<>();
    public static List<String> registerTableType(String name){
        List<String> list = new LinkedList<>();
        TABLE_MAP.put(name,list);
        return list;
    }
    public static void add(String name,String content){
        TABLE_MAP.get(name).add(content);
    }

    public static void print() {
        for (var entry : TABLE_MAP.entrySet()) {
            System.out.println(String.join("\n", entry.getValue()));
        }
    }
    public static void printAll(){
        for(var entry:TABLE_MAP.entrySet()){
            try {
                Files.writeString(
                        Paths.get("./"+entry.getKey()+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())  +".prn"),
                        String.join("\n", entry.getValue())
                );
            } catch (IOException e) {e.printStackTrace();}
        }
    }

}
