package org.example.fake.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name="verification_history")
public class VerificationHistory {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long productId;

    private String result; // ORIGINAL / FAKE
    private String productHash; 
    private LocalDateTime verifiedAt;

    @PrePersist
    public void onCreate(){
        this.verifiedAt = LocalDateTime.now();
    }

    public Long getId(){ return id; }

    public Long getUserId(){ return userId; }
    public void setUserId(Long userId){ this.userId=userId; }

    public Long getProductId(){ return productId; }
    public void setProductId(Long productId){ this.productId=productId; }

    public String getResult(){ return result; }
    public void setResult(String result){ this.result=result; }

    public LocalDateTime getVerifiedAt(){ return verifiedAt; }

	public String getProductHash() {
		return productHash;
	}

	public void setProductHash(String productHash) {
		this.productHash = productHash;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setVerifiedAt(LocalDateTime verifiedAt) {
		this.verifiedAt = verifiedAt;
	}
    
}
