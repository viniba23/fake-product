package org.example.fake.controller;

import java.util.Base64;
import java.util.Optional;

import org.example.fake.model.Product;
import org.example.fake.model.User;
import org.example.fake.repo.ProductQRCodeRepository;
import org.example.fake.service.ProductService;
import org.example.fake.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserProductController {
	@Autowired
    private ProductService productService;
	
	@Autowired
	private UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {

        User user = userService.findByEmail(auth.getName());
        model.addAttribute("user", user);
        model.addAttribute("products", productService.getAllProducts());

        return "user-dashboard";
    }

//    @GetMapping("/products/view/{id}")
//    public String viewProduct(@PathVariable Long id, Model model) {
//        model.addAttribute("product", productService.getProductById(id));
//        return "user-product-view";
//    }
    @Autowired
    private ProductQRCodeRepository qrRepo;

    @GetMapping("/products/view/{id}")
    public String viewProduct(@PathVariable Long id, Model model) {

        Product product = productService.getProductById(id);

        if (product == null) {
            return "redirect:/user/dashboard";
        }

        model.addAttribute("product", product);

        // Load QR if exists
        qrRepo.findByProductId(id).ifPresent(qr -> {
            model.addAttribute("qrImage",
                    Base64.getEncoder().encodeToString(qr.getQrImage()));
        });

        return "user-product-view";
    }

    
}
