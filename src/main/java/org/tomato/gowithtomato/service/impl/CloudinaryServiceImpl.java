package org.tomato.gowithtomato.service.impl;

import com.cloudinary.Cloudinary;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import org.tomato.gowithtomato.service.CloudinaryService;
import org.tomato.gowithtomato.util.CloudinaryUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class CloudinaryServiceImpl implements CloudinaryService {
    private final static CloudinaryServiceImpl INSTANCE = new CloudinaryServiceImpl();
    private static final String FILE_PREFIX = "/tmp";
    private static final int DIRECTORIES_COUNT = 10;
    private final Cloudinary cloudinary;

    private CloudinaryServiceImpl() {
        cloudinary = CloudinaryUtil.getInstance();
    }

    public static CloudinaryServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public String uploadPhoto(HttpServletRequest req) throws ServletException, IOException {
        Part part = req.getPart("image");
        String filename = Paths.get(part.getSubmittedFileName()).getFileName().toString();
        File file = new File(FILE_PREFIX + File.separator + filename.hashCode() % DIRECTORIES_COUNT
                + File.separator + filename);

        InputStream content = part.getInputStream();
        file.getParentFile().mkdirs();
        file.createNewFile();

        FileOutputStream outputStream = new FileOutputStream(file);
        byte[] buffer = new byte[content.available()];
        content.read(buffer);
        outputStream.write(buffer);
        outputStream.close();

        Map<String, Object> uploadResult = cloudinary.uploader().upload(file, new HashMap<>());
        return uploadResult.get("secure_url").toString();
    }
}
