package org.example.fake.repo;

import java.util.Optional;

import org.example.fake.model.BlockchainProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockchainProductRepository 
        extends JpaRepository<BlockchainProduct, Long> {

    Optional<BlockchainProduct> findByProduct_Id(Long productId);

    boolean existsByProductId(Long productId);
    void deleteByProduct_Id(Long productId);
}
