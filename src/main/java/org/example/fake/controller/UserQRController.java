package org.example.fake.controller;

import java.awt.image.BufferedImage;
import java.util.Optional;

import javax.imageio.ImageIO;

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

    // Upload QR Image Scan
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

            return processQR(result.getText(), model);

        } catch (Exception e) {
            model.addAttribute("error", "Invalid QR Code");
            return "user-scan-result";
        }
    }

    // Live Camera Scan
    @GetMapping("/scan-qr-live")
    public String scanLive(@RequestParam("data") String qrText,
                           Model model) {

        return processQR(qrText, model);
    }

//    // Common QR Processing Logic
//    private String processQR(String qrText, Model model) {
//
//        try {
//
//            // Expected Format:
//            // PRODUCT_ID=1|HASH=abc123
//
//            String[] parts = qrText.split("\\|");
//
//            Long productId =
//                    Long.parseLong(parts[0].split("=")[1]);
//
//            String scannedHash =
//                    parts[1].split("=")[1];
//
//            Optional<Product> productOpt =
//                    productRepo.findById(productId);
//
//            if (productOpt.isEmpty()) {
//                model.addAttribute("error", "Product not found!");
//                return "user-scan-result";
//            }
//
//            Optional<BlockchainProduct> bcOpt =
//                    blockchainRepo.findByProduct_Id(productId);
//
//            if (bcOpt.isEmpty()) {
//                model.addAttribute("error",
//                        "Product not enrolled in blockchain!");
//                return "user-scan-result";
//            }
//
//            if (bcOpt.get().getProductHash()
//                    .equals(scannedHash)) {
//
//                model.addAttribute("success",
//                        "AUTHENTIC PRODUCT ✅");
//                model.addAttribute("product",
//                        productOpt.get());
//
//            } else {
//                model.addAttribute("error",
//                        "FAKE PRODUCT ❌");
//            }
//
//        } catch (Exception e) {
//            model.addAttribute("error",
//                    "Invalid QR Format!");
//        }
//
//        return "user-scan-result";
//    }
//    
    
    private String processQR(String qrText, Model model) {

        try {

            // Expected format:
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
                    blockchainRepo.findByProduct_Id(productId);

            if (bcOpt.isEmpty()) {
                model.addAttribute("error",
                        "Product not enrolled in blockchain!");
                return "user-scan-result";
            }

            String originalHash = bcOpt.get().getProductHash();

            if (originalHash.equals(scannedHash)) {

                model.addAttribute("success",
                        "AUTHENTIC PRODUCT ✅");

                model.addAttribute("product",
                        productOpt.get());

                model.addAttribute("productId",
                        productId);

                model.addAttribute("blockchainHash",
                        originalHash);

            } else {

                model.addAttribute("error",
                        "FAKE PRODUCT ❌");

                model.addAttribute("productId",
                        productId);

                model.addAttribute("blockchainHash",
                        scannedHash);
            }

        } catch (Exception e) {

            model.addAttribute("error",
                    "Invalid QR Format!");

        }

        return "user-scan-result";
    }
    @GetMapping("/verify-product")
    public String showVerifyPage() {
        return "user-verify-product";
    }

    @PostMapping("/verify-product")
    public String verifyProduct(@RequestParam Long productId,
                                @RequestParam String productHash,
                                Model model) {

        Optional<Product> productOpt =
                productRepo.findById(productId);

        if (productOpt.isEmpty()) {
            model.addAttribute("error",
                    "Product not found!");
            return "user-verify-result";
        }

        Optional<BlockchainProduct> bcOpt =
                blockchainRepo.findByProduct_Id(productId);

        if (bcOpt.isEmpty()) {
            model.addAttribute("error",
                    "Product not enrolled in blockchain!");
            return "user-verify-result";
        }

        if (bcOpt.get().getProductHash()
                .equals(productHash)) {

            model.addAttribute("success",
                    "AUTHENTIC PRODUCT ✅");
            model.addAttribute("product",
                    productOpt.get());

        } else {

            model.addAttribute("error",
                    "FAKE PRODUCT ❌");

        }

        return "user-verify-result";
    }

}
