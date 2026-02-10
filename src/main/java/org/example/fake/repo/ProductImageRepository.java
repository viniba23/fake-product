package org.example.fake.repo;

import java.util.List;

import org.example.fake.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import jakarta.transaction.Transactional;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long>{
	@Modifying
    @Transactional
    void deleteByIdIn(List<Long> ids);
}
