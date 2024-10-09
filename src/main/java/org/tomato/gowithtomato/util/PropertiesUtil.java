package org.tomato.gowithtomato.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class PropertiesUtil {

    private static final Properties PROPERTIES = new Properties();
    private static final PropertiesUtil INSTANCE = new PropertiesUtil();

    private PropertiesUtil() {
    }

    static {
        loadProperties();
    }

    public static PropertiesUtil getInstance() {
        return INSTANCE;
    }

    private static void loadProperties() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();


        try (InputStream fis = classLoader.getResourceAsStream("application.properties")) {
            PROPERTIES.load(fis);
        } catch (IOException e) {
            log.error("Не удалось загрузить настройки из application.properties");
        }
    }

    public String get(String key) {
        return PROPERTIES.getProperty(key);
    }
}
