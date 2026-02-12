package org.example.fake.controller;

import java.io.IOException;
import java.util.Optional;

import org.example.fake.model.BlockchainProduct;
import org.example.fake.model.Product;
import org.example.fake.repo.BlockchainProductRepository;
import org.example.fake.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

@Controller
@RequestMapping("/user")
public class UserQRController {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private BlockchainProductRepository blockchainRepo;

    // Show scan page
    @GetMapping("/scan-qr")
    public String showScanPage() {
        return "user-scan-qr";
    }

    // Handle uploaded QR
    @PostMapping("/scan-qr")
    public String scanQR(@RequestParam("file") MultipartFile file,
                         Model model) {

        try {

            BufferedImage bufferedImage =
                    ImageIO.read(file.getInputStream());

            LuminanceSource source =
                    new BufferedImageLuminanceSource(bufferedImage);

            BinaryBitmap bitmap =
                    new BinaryBitmap(new HybridBinarizer(source));

            Result result =
                    new MultiFormatReader().decode(bitmap);

            String qrText = result.getText();

            // Example:
            // PRODUCT_ID=1|HASH=abc123

            String[] parts = qrText.split("\\|");

            Long productId =
                    Long.parseLong(parts[0].split("=")[1]);

            String scannedHash =
                    parts[1].split("=")[1];

            Optional<Product> productOpt =
                    productRepo.findById(productId);

            if (productOpt.isEmpty()) {
                model.addAttribute("error", "Product not found!");
                return "user-scan-result";
            }

            Optional<BlockchainProduct> bcOpt =
                    blockchainRepo.findAll().stream()
                    .filter(b -> b.getProduct().getId().equals(productId))
                    .findFirst();

            if (bcOpt.isEmpty()) {
                model.addAttribute("error",
                        "Product not enrolled in blockchain!");
                return "user-scan-result";
            }

            if (bcOpt.get().getProductHash().equals(scannedHash)) {

                model.addAttribute("success",
                        "AUTHENTIC PRODUCT ✅");
                model.addAttribute("product",
                        productOpt.get());

            } else {
                model.addAttribute("error",
                        "FAKE PRODUCT ❌");
            }

        } catch (Exception e) {
            model.addAttribute("error",
                    "Invalid QR Code");
        }

        return "user-scan-result";
    }
}
