package org.example.fake.service;

import java.util.List;

import org.example.fake.model.Review;
import org.example.fake.repo.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
	 @Autowired
	    private ReviewRepository reviewRepo;

	    public void saveReview(Review review) {
	        reviewRepo.save(review);
	    }

	    public List<Review> getReviewsByProduct(Long productId) {
	        return reviewRepo.findByProductId(productId);
	    }

	    public boolean alreadyReviewed(Long userId, Long productId) {
	        return reviewRepo.existsByUserIdAndProductId(userId, productId);
	    }
	    public List<Review> getPendingReviews() {
	        return reviewRepo.findByStatus("PENDING");
	    }

	    public List<Review> getApprovedReviewsByProduct(Long productId) {
	        return reviewRepo.findByProductIdAndStatus(productId, "APPROVED");
	    }

	    public void approveReview(Long id) {
	        Review review = reviewRepo.findById(id).orElse(null);

	        if (review != null) {
	            review.setStatus("APPROVED");
	            reviewRepo.save(review);   // MUST SAVE AGAIN
	        }
	    }
}
