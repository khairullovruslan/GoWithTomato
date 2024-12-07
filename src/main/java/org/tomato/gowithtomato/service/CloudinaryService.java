package org.tomato.gowithtomato.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public interface CloudinaryService {
    String uploadPhoto(HttpServletRequest req) throws ServletException, IOException;
}
