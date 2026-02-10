package org.example.fake.controller;

import org.example.fake.service.BlockchainService;
import org.example.fake.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/blockchain")
public class BlockchainController {

    @Autowired
    private ProductService productService;

    @Autowired
    private BlockchainService blockchainService;

    @GetMapping
    public String showBlockchainPage(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin-blockchain-enroll";
    }

    @PostMapping("/enroll/{id}")
    public String enrollProduct(@PathVariable Long id, Model model) {
        try {
            blockchainService.enrollProduct(id);
            model.addAttribute("success", "Product enrolled in blockchain successfully");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }

        model.addAttribute("products", productService.getAllProducts());
        return "admin-blockchain-enroll";
    }
}
