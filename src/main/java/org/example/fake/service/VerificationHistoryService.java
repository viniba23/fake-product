package org.example.fake.service;

import java.util.List;

import org.example.fake.model.VerificationHistory;
import org.example.fake.repo.VerificationHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerificationHistoryService {
	 @Autowired
	    private VerificationHistoryRepository repo;

	    public void saveHistory(VerificationHistory history){
	        repo.save(history);
	    }

	    public List<VerificationHistory> getUserHistory(Long userId){
	        return repo.findByUserId(userId);
}
	    }
