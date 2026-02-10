package org.example.fake.repo;

import java.util.Optional;

import org.example.fake.model.ProductQRCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductQRCodeRepository extends JpaRepository<ProductQRCode, Long>{
	boolean existsByProductId(Long productId);

    Optional<ProductQRCode> findByProductId(Long productId);
}
