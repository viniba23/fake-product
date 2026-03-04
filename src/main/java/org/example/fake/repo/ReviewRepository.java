package org.example.fake.repo;

import java.util.List;

import org.example.fake.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long>{
	List<Review> findByProductId(Long productId);

    boolean existsByUserIdAndProductId(Long userId, Long productId);
    List<Review> findByStatus(String status);

    List<Review> findByProductIdAndStatus(Long productId, String status);
    List<Review> findByUserId(Long userId);
}
