package org.example.fake.repo;

import java.util.List;

import org.example.fake.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long>{
	List<Purchase> findByUserId(Long userId);
}
