package org.example.fake.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "reviews")
public class Review {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private Long userId;
	    private Long productId;
	   

	    private int rating; // 1 to 5

	    @Column(length = 1000)
	    private String comment;

	    private LocalDateTime reviewDate;

//	    @PrePersist
//	    public void onCreate() {
//	        this.reviewDate = LocalDateTime.now();
//	    }
	    @Column(length = 20)
	    private String status;

	    @PrePersist
	    public void onCreate() {
	        this.reviewDate = LocalDateTime.now();
	        this.status = "PENDING";   // default
	    }

	    public String getStatus() { return status; }
	    public void setStatus(String status) { this.status = status; }
	    // Getters & Setters
	    public Long getId() { return id; }

	    public Long getUserId() { return userId; }
	    public void setUserId(Long userId) { this.userId = userId; }

	    public Long getProductId() { return productId; }
	    public void setProductId(Long productId) { this.productId = productId; }

	    public int getRating() { return rating; }
	    public void setRating(int rating) { this.rating = rating; }

	    public String getComment() { return comment; }
	    public void setComment(String comment) { this.comment = comment; }

	    public LocalDateTime getReviewDate() { return reviewDate; }
	    
	    

}
