//package com.example.demo.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import com.example.demo.model.Admin;
//import com.example.demo.service.AdminService;
//
//@Controller
//@RequestMapping("/admin")
//public class AdminController {
//    @Autowired
//    private AdminService adminService;
//
//    @GetMapping("/login")
//    public String loginPage() {
//    	System.out.println("admin from get login");
//
//        return "admin-login";
//    }
//
//    @PostMapping("/login")
//    public String login(@RequestParam String email,
//                       @RequestParam String password,
//                       Model model) {
//    	System.out.println("admin from get login");
//
//        Admin admin = adminService.authenticate(email, password);
//        if (admin != null) {
//            return "admin-dashboard";
//        }
//        model.addAttribute("error", "Invalid credentials");
//        return "admin-login";
//    }
//}
package org.example.fake.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.example.fake.service.AdminService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                           Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid credentials");
        }
        return "admin-login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                       @RequestParam String password,
                       Model model) {
        if (adminService.authenticate(email, password) != null) {
            return "admin-dashboard";
        }
        return "redirect:/admin/login?error=true";
    }
    
    @GetMapping("/forgot-password")
    public String showAdminForgotPasswordForm(Model model) {
        model.addAttribute("message", null);
        model.addAttribute("error", null);
        return "admin-forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processAdminForgotPassword(@RequestParam("email") String email, Model model) {
        try {
            adminService.initiateAdminPasswordReset(email);
            model.addAttribute("email", email);
            model.addAttribute("message", "We have sent a password reset OTP to your email");
            return "admin-verify-otp";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "admin-forgot-password";
        }
    }

    @GetMapping("/verify-otp")
    public String showAdminVerifyOtpForm(@RequestParam(value = "email", required = false) String email, Model model) {
        if (email != null) {
            model.addAttribute("email", email);
        }
        return "admin-verify-otp";
    }

    @PostMapping("/verify-otp")
    public String processAdminVerifyOtp(
            @RequestParam("token") String token,
            @RequestParam("email") String email,
            Model model) {
        
        if (adminService.validateAdminResetToken(token, email)) {
            model.addAttribute("token", token);
            model.addAttribute("email", email);
            return "admin-reset-password";
        }
        
        model.addAttribute("error", "Invalid or expired OTP");
        model.addAttribute("email", email);
        return "admin-verify-otp";
    }

    @GetMapping("/reset-password")
    public String showAdminResetPasswordForm(
            @RequestParam("token") String token,
            @RequestParam("email") String email,
            Model model) {
        model.addAttribute("token", token);
        model.addAttribute("email", email);
        return "admin-reset-password";
    }

    @PostMapping("/reset-password")
    public String processAdminResetPassword(
            @RequestParam("token") String token,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            Model model) {
        
        try {
            adminService.resetAdminPassword(token, email, password);
            model.addAttribute("message", "Password reset successfully. Please login with your new password");
            return "redirect:/admin/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("token", token);
            model.addAttribute("email", email);
            return "admin-reset-password";
        }
    }
}
