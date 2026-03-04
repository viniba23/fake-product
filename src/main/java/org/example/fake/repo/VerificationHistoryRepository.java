package org.example.fake.repo;

import java.util.List;

import org.example.fake.model.VerificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationHistoryRepository extends JpaRepository<VerificationHistory,Long>{
	List<VerificationHistory> findByUserId(Long userId);

}
