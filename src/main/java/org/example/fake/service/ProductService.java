package org.example.fake.service;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.example.fake.model.Product;
import org.example.fake.model.ProductImage;
import org.example.fake.repo.BlockchainProductRepository;
import org.example.fake.repo.CartRepository;
import org.example.fake.repo.ProductImageRepository;
import org.example.fake.repo.ProductQRCodeRepository;
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
	private ProductQRCodeRepository productQRCodeRepository;
	
	@Autowired
    private ProductImageRepository productImageRepository ;
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private BlockchainProductRepository blockchainProductRepository;

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

    public Product getProductById(Long id) {

        Optional<Product> optional = productRepository.findById(id);

        if(optional.isEmpty()){
            return null;
        }

        Product product = optional.get();

        if(product.getImages() != null){
            product.getImages().forEach(img -> {
                img.setBase64Image(
                    Base64.getEncoder().encodeToString(img.getImageData())
                );
            });
        }

        return product;
    }
//    public void deleteProduct(Long id) {
//        productRepository.deleteById(id);
//    }
    public void deleteProduct(Long id) {

        // delete cart items
        cartRepository.deleteByProduct_Id(id);

        // delete qr codes
        productQRCodeRepository.deleteByProduct_Id(id);

        // delete blockchain records
        blockchainProductRepository.deleteByProduct_Id(id);

        // delete images
        productImageRepository.deleteByProduct_Id(id);

        // finally delete product
        productRepository.deleteById(id);
    }
//    public void deleteImagesByIds(List<Long> imageIds) {
//        productImageRepository.deleteByIdIn(imageIds);
//    }

    public List<Product> getOutOfStockProducts() {
        return productRepository.findByQuantityLessThanEqual(0);
    }
    
}
