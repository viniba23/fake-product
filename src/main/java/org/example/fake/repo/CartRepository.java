package org.example.fake.repo;

import java.util.List;

import org.example.fake.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long>{
	List<Cart> findByUserId(Long userId);
}
