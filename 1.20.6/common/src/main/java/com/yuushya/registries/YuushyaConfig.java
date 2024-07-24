package com.yuushya.registries;

import com.yuushya.utils.GsonTools;
import dev.architectury.platform.Platform;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class YuushyaConfig {
    public static class Config{
        public boolean self = true;
    }
    public static Config CONFIG= new Config();
    public static final File CONFIG_FILE= Platform.getConfigFolder().resolve("com.yuushya/config.json").toFile();
    public static void readConfig(){
        if(CONFIG_FILE.exists()){
            try (BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE))) {
                CONFIG = GsonTools.NormalGSON.fromJson(reader, Config.class);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
