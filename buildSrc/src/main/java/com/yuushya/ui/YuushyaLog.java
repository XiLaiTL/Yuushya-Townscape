package com.yuushya.ui;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class YuushyaLog {
    public static List<String> LOG = new ArrayList<>();
    public static void add(Exception e){
        LOG.add(e.getMessage()+"\n"+String.join("\n", Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList())+"\n"+e.getLocalizedMessage()+"\n");
    }

    public static void print(){
        if(!LOG.isEmpty()){
            try {
                Files.writeString(
                        Paths.get("./"+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())  +".log"),
                        String.join("\n", LOG)
                );
            } catch (IOException e) {e.printStackTrace();}
        }
    }
}
