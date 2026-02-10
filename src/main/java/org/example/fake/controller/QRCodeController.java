package org.example.fake.controller;

import java.util.Base64;

import org.example.fake.service.ProductService;
import org.example.fake.service.QRCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/qr")
public class QRCodeController {
	 @Autowired
	    private ProductService productService;

	    @Autowired
	    private QRCodeService qrService;

	    @GetMapping
	    public String showQRPage(Model model) {
	        model.addAttribute("products", productService.getAllProducts());
	        return "admin-qr-generate";
	    }

	    @PostMapping("/generate/{id}")
	    public String generateQR(@PathVariable Long id, Model model) {
	        try {
	            qrService.generateQRCode(id);
	            model.addAttribute("success", "QR Code generated successfully");
	        } catch (Exception e) {
	            model.addAttribute("error", e.getMessage());
	        }
	        model.addAttribute("products", productService.getAllProducts());
	        return "admin-qr-generate";
	    }

	    @GetMapping("/view/{id}")
	    public String viewQR(@PathVariable Long id, Model model) {
	        qrService.getQRCode(id).ifPresent(qr -> {
	            model.addAttribute("qrImage",
	                Base64.getEncoder().encodeToString(qr.getQrImage()));
	        });
	        return "admin-qr-view";
	    }
	
}
