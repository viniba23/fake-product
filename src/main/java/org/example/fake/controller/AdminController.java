package org.example.fake.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.example.fake.model.User;
import org.example.fake.repo.UserRepository;
import org.example.fake.service.AdminService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    
    @Autowired
    private UserRepository userRepository;

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
    
    @GetMapping("/users")
    public String viewUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin-users";
    }

    @GetMapping("/users/deactivate/{id}")
    public String deactivateUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow();
        user.setActive(false);
        userRepository.save(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/activate/{id}")
    public String activateUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow();
        user.setActive(true);
        userRepository.save(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/view/{id}")
    public String viewUser(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id).orElseThrow();
        model.addAttribute("user", user);
        return "admin-user-view";
    }
}
