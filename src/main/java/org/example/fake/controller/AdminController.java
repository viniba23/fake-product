package org.example.fake.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.fake.model.Cart;
import org.example.fake.model.Product;
import org.example.fake.model.Purchase;
import org.example.fake.model.Review;
import org.example.fake.model.User;
import org.example.fake.model.VerificationHistory;
import org.example.fake.repo.UserRepository;
import org.example.fake.service.AdminService;
import org.example.fake.service.CartService;
import org.example.fake.service.ProductService;
import org.example.fake.service.PurchaseService;
import org.example.fake.service.ReviewService;
import org.example.fake.service.UserService;
import org.example.fake.service.VerificationHistoryService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CartService cartService;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;
    @Autowired
    private ReviewService reviewService;
    
    @Autowired
    private VerificationHistoryService verificationHistoryService;
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                           Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid credentials");
        }
        return "admin-login";
    }
    
//    @GetMapping("/dashboard")
//    public String showDashboard() {
//        return "admin-dashboard";
//    }

//    @PostMapping("/login")
//    public String login(@RequestParam String email,
//                       @RequestParam String password,
//                       Model model) {
//        if (adminService.authenticate(email, password) != null) {
//            return "admin-dashboard";
//        }
//        return "redirect:/admin/login?error=true";
//    }
    @PostMapping("/login")
    public String login(@RequestParam String email,
                       @RequestParam String password,
                       Model model) {

        if (adminService.authenticate(email, password) != null) {
            return "redirect:/admin/dashboard";
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
    
    @GetMapping("/cart-details")
    public String viewAllCartDetails(Model model) {

        List<Cart> cartItems = cartService.getAllCartItems();

        Map<Long, User> userMap = new HashMap<>();

        for (Cart c : cartItems) {
            User user = userService.getUserById(c.getUserId());
            userMap.put(c.getUserId(), user);
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("userMap", userMap);

        return "admin-cart-details";
    }
    
    @GetMapping("/purchase-details")
    public String viewAllPurchases(Model model) {

        List<Purchase> purchases = purchaseService.getAllPurchases();

        Map<Long, User> userMap = new HashMap<>();

        for (Purchase p : purchases) {
            User user = userService.getUserById(p.getUserId());
            userMap.put(p.getUserId(), user);
        }

        model.addAttribute("purchases", purchases);
        model.addAttribute("userMap", userMap);

        return "admin-purchase-details";
    }
    
    @GetMapping("/reviews")
    public String viewPendingReviews(Model model) {

        List<Review> reviews = reviewService.getPendingReviews();

        Map<Long, User> userMap = new HashMap<>();
        Map<Long, Product> productMap = new HashMap<>();

        for (Review r : reviews) {
            userMap.put(r.getUserId(),
                    userService.getUserById(r.getUserId()));

            productMap.put(r.getProductId(),
                    productService.getProductById(r.getProductId()));
        }

        model.addAttribute("reviews", reviews);
        model.addAttribute("userMap", userMap);
        model.addAttribute("productMap", productMap);

        return "admin-review-list";
    }
    
    @PostMapping("/reviews/approve")
    public String approveReview(@RequestParam Long reviewId) {

        reviewService.approveReview(reviewId);

        return "redirect:/admin/reviews";
    }
    @GetMapping("/reviews/approved")
    public String viewApprovedReviews(Model model) {

        List<Review> reviews =
                reviewService.getReviewsByStatus("APPROVED");

        Map<Long, User> userMap = new HashMap<>();
        Map<Long, Product> productMap = new HashMap<>();

        for (Review r : reviews) {
            userMap.put(r.getUserId(),
                    userService.getUserById(r.getUserId()));

            productMap.put(r.getProductId(),
                    productService.getProductById(r.getProductId()));
        }

        model.addAttribute("reviews", reviews);
        model.addAttribute("userMap", userMap);
        model.addAttribute("productMap", productMap);

        return "admin-approved-reviews";
    }
    @PostMapping("/reviews/delete")
    public String deleteReview(@RequestParam Long reviewId) {

        reviewService.deleteReview(reviewId);

        return "redirect:/admin/reviews/approved";
    }
    @PostMapping("/reviews/delete-pending")
    public String deletePendingReview(@RequestParam Long reviewId) {

        reviewService.deleteReview(reviewId);

        return "redirect:/admin/reviews";
    }
    
    @GetMapping("/verification-logs")
    public String verificationLogs(Model model) {

        List<VerificationHistory> logs =
                verificationHistoryService.getAllHistory();

        Map<Long, Product> productMap = new HashMap<>();
        Map<Long, User> userMap = new HashMap<>();

        for(VerificationHistory h : logs){

            Product product =
                    productService.getProductById(h.getProductId());

            User user =
                    userService.getUserById(h.getUserId());

            productMap.put(h.getProductId(), product);
            userMap.put(h.getUserId(), user);
        }

        model.addAttribute("logs", logs);
        model.addAttribute("productMap", productMap);
        model.addAttribute("userMap", userMap);

        return "admin-verification-logs";
    }
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {

        List<Product> outOfStockProducts = productService.getOutOfStockProducts();

        if(outOfStockProducts == null){
            outOfStockProducts = new java.util.ArrayList<>();
        }

        model.addAttribute("outOfStockProducts", outOfStockProducts);
        model.addAttribute("notificationCount", outOfStockProducts.size());

        return "admin-dashboard";
    }
}
