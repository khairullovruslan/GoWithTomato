package org.tomato.gowithtomato.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.tomato.gowithtomato.exception.db.UserNotFoundException;
import org.tomato.gowithtomato.service.CookieService;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@WebFilter("/*")
public class AuthFilter extends HttpFilter {
    private CookieService cookieService;

    private static final Set<String> pagesForAuthorized = new HashSet<>(
            List.of("")
    );

    @Override
    public void init() throws ServletException {
        cookieService = CookieService.getInstance();
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {

        try {
            req.setAttribute("user", cookieService.findUser(req));
            super.doFilter(req, res, chain);
        } catch (UserNotFoundException e) {
            String path = req.getRequestURI().substring(req.getContextPath().length());
            if (pagesForAuthorized.contains(path)) {
                throw e;
            } else {
                req.setAttribute("user", null);
            }

        }

    }


}
