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
        replaceEnvVariables();
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

    private static void replaceEnvVariables() {
        for (String name : PROPERTIES.stringPropertyNames()) {
            String value = PROPERTIES.getProperty(name);
            if (value != null) {
                value = value.replace("${PROD_DB_HOST}", System.getenv("PROD_DB_HOST"))
                        .replace("${PROD_DB_PORT}", System.getenv("PROD_DB_PORT"))
                        .replace("${PROD_DB_NAME}", System.getenv("PROD_DB_NAME"))
                        .replace("${PROD_DB_PASSWORD}", System.getenv("PROD_DB_PASSWORD"))
                        .replace("${PROD_DB_USERNAME}", System.getenv("PROD_DB_USERNAME"))
                        .replace("${PROD_CLOUDINARY_CLOUD_NAME}", System.getenv("PROD_CLOUDINARY_CLOUD_NAME"))
                        .replace("${PROD_CLOUDINARY_API_KEY}", System.getenv("PROD_CLOUDINARY_API_KEY"))
                        .replace("${PROD_CLOUDINARY_API_SECRET}", System.getenv("PROD_CLOUDINARY_API_SECRET"))
                        .replace("${PROD_GRAPH_HOPPER_API_KEY}", System.getenv("PROD_GRAPH_HOPPER_API_KEY"));

                PROPERTIES.setProperty(name, value);
            }
        }
    }
}
