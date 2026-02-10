package org.example.fake.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.UUID;

import org.example.fake.model.BlockchainProduct;
import org.example.fake.model.Product;
import org.example.fake.repo.BlockchainProductRepository;
import org.example.fake.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class BlockchainService {

	@Autowired
    private BlockchainProductRepository blockchainRepo;

    @Autowired
    private ProductRepository productRepo;

    public void enrollProduct(Long productId) throws Exception {

        if (blockchainRepo.existsByProductId(productId)) {
            throw new RuntimeException("Product already enrolled in blockchain");
        }

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        String productHash = generateHash(
                product.getId() +
                product.getProductName() +
                product.getManufacturer() +
                product.getManufacturingDate()
        );

        BlockchainProduct bp = new BlockchainProduct();
        bp.setProduct(product);
        bp.setProductHash(productHash);

        // 🔹 Simulated blockchain values
        bp.setBlockNumber("BLOCK-" + UUID.randomUUID().toString().substring(0, 8));
        bp.setTransactionHash("TX-" + UUID.randomUUID().toString());

        blockchainRepo.save(bp);
    }

    private String generateHash(String input) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));

        StringBuilder hex = new StringBuilder();
        for (byte b : hashBytes) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }
}
