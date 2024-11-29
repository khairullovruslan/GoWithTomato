package org.tomato.gowithtomato.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.tomato.gowithtomato.service.AuthService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@WebFilter("/*")
public class AuthFilter extends HttpFilter {
    private AuthService authService;

    @Override
    public void init() throws ServletException {
        super.init();
        authService = (AuthService) this.getServletContext().getAttribute("authService");
    }

    private static final Map<String, List<String>> pagesForAuthorizedUser = new HashMap<>(Map.of(
            "PUT", List.of("/trip/.*"),
            "DELETE", List.of(),
            "POST", List.of("/review", "/new-route", "/create-trip", "/trip/.*"),
            "GET", List.of("/review", "/new-route", "/profile/routes", "/create-trip"),
            "PATCH", List.of()));


    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        boolean isPagesForAuthorizedUser = false;
        System.out.println(req.getRequestURI().substring(req.getContextPath().length()));
        for (String s : pagesForAuthorizedUser.get(req.getMethod())) {
            Pattern pattern = Pattern.compile(s);
            if (pattern.matcher(req.getRequestURI().substring(req.getContextPath().length())).matches()) {
                isPagesForAuthorizedUser = true;
            }
        }
        log.info("isAuthorizedPage? : {}", isPagesForAuthorizedUser);
        if (isPagesForAuthorizedUser && authService.authorizationCheck(req)) {
            chain.doFilter(req, res);
        } else if (isPagesForAuthorizedUser && req.getMethod().equals("GET")) {
            req.getRequestDispatcher("/templates/login.jsp").forward(req, res);
        } else if (isPagesForAuthorizedUser) {
            res.sendRedirect(req.getContextPath() + "/login");
        } else {
            chain.doFilter(req, res);
        }

    }


}
