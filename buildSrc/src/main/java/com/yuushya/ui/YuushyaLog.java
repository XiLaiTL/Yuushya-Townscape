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
    private static final List<String> LOG_ERROR = new ArrayList<>();
    private static final List<String> LOG_WARN = new ArrayList<>();
    private static final List<String> LOG_DEBUG = new ArrayList<>();
    public static void error(Exception e){
        LOG_ERROR.add(e.getMessage()+"\n"+String.join("\n", Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList())+"\n"+e.getLocalizedMessage()+"\n");
    }
    public static void warn(String e){ LOG_WARN.add(e); }
    public static void debug(String e){ LOG_DEBUG.add(e); }

    public static void print(){
        List<String> all_information = new ArrayList<>();
        all_information.addAll(LOG_DEBUG);
        all_information.addAll(LOG_WARN);
        all_information.addAll(LOG_ERROR);
        if(!all_information.isEmpty()){
            try {
                Files.writeString(
                        Paths.get("./"+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())  +".log"),
                        String.join("\n", all_information)
                );
            } catch (IOException e) {e.printStackTrace();}
        }
        Mode.printAll();
    }
}
