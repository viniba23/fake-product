package org.example.fake.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Controller
public class LogoutController {
	@PostMapping("/logout-success")
    public void logoutRedirect(Authentication authentication,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {

        String type = request.getParameter("logoutType");

        if ("admin".equals(type)) {
            response.sendRedirect("/admin/login?logout");
        } else {
            response.sendRedirect("/user/login?logout");
        }
    }
}
