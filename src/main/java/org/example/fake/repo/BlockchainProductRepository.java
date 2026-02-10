package org.example.fake.repo;

import org.example.fake.model.BlockchainProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockchainProductRepository extends JpaRepository<BlockchainProduct, Long>{
	boolean existsByProductId(Long productId);
}
