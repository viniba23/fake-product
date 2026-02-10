package org.example.fake.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.example.fake.model.Product;
import org.example.fake.model.ProductImage;
import org.example.fake.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin/products")
public class ProductController {
	@Autowired
    private ProductService productService;

    @GetMapping("/add")
    public String showAddProductPage() {
        return "admin-add-product";
    }

    @PostMapping("/add")
    public String saveProduct(
            @RequestParam String productName,
            @RequestParam double price,
            @RequestParam int quantity,
            @RequestParam String manufacturer,
            @RequestParam LocalDate manufacturingDate,
            @RequestParam(required = false) String description,
            @RequestParam("images") MultipartFile[] images,
            Model model) {

        try {
            Product product = new Product();
            product.setProductName(productName);
            product.setPrice(price);
            product.setQuantity(quantity);
            product.setManufacturer(manufacturer);
            product.setManufacturingDate(manufacturingDate);
            product.setDescription(description);

            List<ProductImage> imageList = new ArrayList<>();

            for (MultipartFile file : images) {
                if (!file.isEmpty()) {
                    ProductImage img = new ProductImage();
                    img.setImageData(file.getBytes());
                    img.setProduct(product);
                    imageList.add(img);
                }
            }

            product.setImages(imageList);

            productService.saveProduct(product);

            model.addAttribute("success", "Product registered successfully!");
            return "admin-add-product";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Failed to save product images");
            return "admin-add-product";
        }
    }
    
    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin-manage-products";
    }

    @GetMapping("/view/{id}")
    public String viewProduct(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        return "admin-view-product";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }
    @GetMapping("/edit/{id}")
    public String showEditProduct(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        return "admin-edit-product";
    }


    @PostMapping("/edit/{id}")
    public String updateProduct(
            @PathVariable Long id,
            @RequestParam String productName,
            @RequestParam double price,
            @RequestParam int quantity,
            @RequestParam String manufacturer,
            @RequestParam LocalDate manufacturingDate,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) List<Long> removeImageIds,
            @RequestParam(required = false) MultipartFile[] images
    ) throws Exception {

        Product product = productService.getProductById(id);

        product.setProductName(productName);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setManufacturer(manufacturer);
        product.setManufacturingDate(manufacturingDate);
        product.setDescription(description);

        // ✅ REMOVE SELECTED OLD IMAGES
        if (removeImageIds != null && !removeImageIds.isEmpty()) {
            productService.deleteImagesByIds(removeImageIds);
        }

        // ✅ ADD NEW IMAGES
        if (images != null) {
            for (MultipartFile file : images) {
                if (!file.isEmpty()) {
                    ProductImage img = new ProductImage();
                    img.setImageData(file.getBytes());
                    img.setProduct(product);
                    product.getImages().add(img);
                }
            }
        }

        productService.saveProduct(product);
        return "redirect:/admin/products";
    }


}
