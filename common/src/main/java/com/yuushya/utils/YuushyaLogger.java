package com.yuushya.utils;

import com.yuushya.Yuushya;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class YuushyaLogger {
    private static final Logger LOGGER = LogManager.getLogger(Yuushya.MOD_ID);

    public static void info(Object object) {
        LOGGER.info(String.valueOf(object));
    }

    public static void debug(Object object) {
        LOGGER.debug(String.valueOf(object));
    }

    public static void warn(Object object) {LOGGER.warn(String.valueOf(object));}

    public static void error(Object object) {LOGGER.error(String.valueOf(object));}

}
