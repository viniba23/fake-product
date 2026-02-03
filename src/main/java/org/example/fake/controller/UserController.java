package org.example.fake.controller;

import org.example.fake.model.User;
import org.example.fake.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Login page - only GET method needed (POST handled by Spring Security)
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                           @RequestParam(value = "logout", required = false) String logout,
                           Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid email or password");
        }
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully");
        }
        return "user-login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/user/login";
        }
        
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        
        if (user == null) {
            return "redirect:/user/login";
        }
        
        model.addAttribute("user", user);
        return "user-dashboard";
    }

    // Registration
    @GetMapping("/register")
    public String registerPage() {
        return "user-register";
    }

    @PostMapping("/register")
    public String register(User user, @RequestParam String confirmPassword, Model model) {
        try {
            if (!user.getPassword().equals(confirmPassword)) {
                model.addAttribute("error", "Passwords don't match");
                return "user-register";
            }
            userService.registerUser(user);
            model.addAttribute("message", "Registration successful! Please login.");
            return "redirect:/user/login";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "user-register";
        }
    }


    @GetMapping("/forgot-password")
    public String showForgotPasswordForm(Model model) {
        model.addAttribute("message", null);
        model.addAttribute("error", null);
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, Model model) {
        try {
        	 System.out.println("[CONTROLLER] Forgot password request received for email: " + email);
            userService.initiatePasswordReset(email);
            model.addAttribute("email", email);
            model.addAttribute("message", "We have sent a password reset OTP to your email");
            return "verify-otp";
        } catch (Exception e) {
        	System.out.println("[CONTROLLER] Error: " + e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "forgot-password";
        }
    }

    // OTP Verification
    @GetMapping("/verify-otp")
    public String showVerifyOtpForm(@RequestParam(value = "email", required = false) String email, Model model) {
        if (email != null) {
            model.addAttribute("email", email);
        }
        return "verify-otp";
    }

    @PostMapping("/verify-otp")
    public String processVerifyOtp(
            @RequestParam("token") String token,
            @RequestParam("email") String email,
            Model model) {
        
        if (userService.validateResetToken(token, email)) {
            model.addAttribute("token", token);
            model.addAttribute("email", email);
            return "reset-password";
        }
        
        model.addAttribute("error", "Invalid or expired OTP");
        model.addAttribute("email", email);
        return "verify-otp";
    }

    // Password Reset
    @GetMapping("/reset-password")
    public String showResetPasswordForm(
            @RequestParam("token") String token,
            @RequestParam("email") String email,
            Model model) {
        model.addAttribute("token", token);
        model.addAttribute("email", email);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(
            @RequestParam("token") String token,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            Model model) {
        
        try {
            userService.resetPassword(token, email, password);
            model.addAttribute("message", "Password reset successfully. Please login with your new password");
            return "user-login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("token", token);
            model.addAttribute("email", email);
            return "reset-password";
        }
    }

    // Logout is handled by Spring Security config
}
