package org.tomato.gowithtomato.util;


import com.cloudinary.Cloudinary;

import java.util.HashMap;
import java.util.Map;

public class CloudinaryUtil {

    private static Cloudinary cloudinary;

    public static Cloudinary getInstance() {
        if (cloudinary == null) {
            Map<String, String> configMap = new HashMap<>();
            configMap.put("cloud_name", System.getenv("cloudinary.cloud_name"));
            configMap.put("api_key", System.getenv("cloudinary.api_key"));
            configMap.put("api_secret", System.getenv("cloudinary.api_secret"));
            cloudinary = new Cloudinary(configMap);
        }
        return cloudinary;
    }
}