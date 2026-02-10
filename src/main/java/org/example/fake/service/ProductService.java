package org.example.fake.service;

import java.util.Base64;
import java.util.List;

import org.example.fake.model.Product;
import org.example.fake.repo.ProductImageRepository;
import org.example.fake.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
@Service
@Transactional
public class ProductService {
	@Autowired
    private ProductRepository productRepository;
	
	@Autowired
    private ProductImageRepository productImageRepository ;

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
//    public List<Product> getAllProducts() {
//        return productRepository.findAll();
//    }
//
//    public Product getProductById(Long id) {
//        Product product = productRepository.findById(id).orElseThrow();
//
//        if (product.getImages() != null) {
//            product.getImages().forEach(img -> {
//                String base64 = Base64.getEncoder().encodeToString(img.getImageData());
//                img.setBase64Image(base64);
//            });
//        }
//
//        return product;
//    }
    
    // ✅ USED BY DASHBOARDS (ADMIN + USER)
    public List<Product> getAllProducts() {

        List<Product> products = productRepository.findAll();

        for (Product product : products) {
            if (product.getImages() != null) {
                product.getImages().forEach(img -> {
                    img.setBase64Image(
                        Base64.getEncoder().encodeToString(img.getImageData())
                    );
                });
            }
        }

        return products;
    }

    // ✅ USED BY VIEW / EDIT PAGES
    public Product getProductById(Long id) {

        Product product = productRepository.findById(id).orElseThrow();

        if (product.getImages() != null) {
            product.getImages().forEach(img -> {
                img.setBase64Image(
                    Base64.getEncoder().encodeToString(img.getImageData())
                );
            });
        }

        return product;
    }
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    

//    public void deleteImagesByIds(List<Long> imageIds) {
//        productImageRepository.deleteByIdIn(imageIds);
//    }

    
    
}
