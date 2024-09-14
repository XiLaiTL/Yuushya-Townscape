package com.yuushya.registries;

import com.yuushya.utils.GsonTools;
import dev.architectury.platform.Platform;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;

public class YuushyaConfig {
    interface CommonConfig<T extends CommonConfig<T>>{
        Path configPath();
        Class<T> getSerializedClass();
        void setConfig(T instance);
        T getConfig();

        default void read(){
            if(Files.exists(configPath())){
                try (BufferedReader reader = Files.newBufferedReader(configPath())) {
                    setConfig(GsonTools.NormalGSON.fromJson(reader, getSerializedClass()));
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        default void update(){
            if(!Files.exists(configPath())){
                try {
                    Files.createDirectories(configPath().getParent());
                    Files.createFile(configPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(Files.exists(configPath())){
                try(BufferedWriter writer = Files.newBufferedWriter(configPath())){
                    String json = GsonTools.NormalGSON.toJson(getConfig(),getSerializedClass());
                    writer.write(json);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Config CONFIG= new Config();
    public static class Config implements CommonConfig<Config>{
        public boolean self = true;

        public static final Path CONFIG_FILE= Platform.getConfigFolder().resolve("com.yuushya/config.json");
        @Override public Path configPath() { return CONFIG_FILE; }
        @Override public Class<Config> getSerializedClass() {return Config.class;}
        @Override public void setConfig(Config instance) { CONFIG = instance; }
        @Override public Config getConfig(){return this;}
    }

    public static ClientConfig CLIENT_CONFIG = new ClientConfig();
    public static class ClientConfig implements CommonConfig<ClientConfig>{
        public boolean check = true;

        public static final Path CLIENT_CONFIG_FILE = Platform.getConfigFolder().resolve("com.yuushya/client_config.json");
        @Override public Path configPath() { return CLIENT_CONFIG_FILE; }
        @Override public Class<ClientConfig> getSerializedClass() {return ClientConfig.class;}
        @Override public void setConfig(ClientConfig instance) { CLIENT_CONFIG = instance; }
        @Override public ClientConfig getConfig(){return this;}
    }



}
