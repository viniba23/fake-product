package org.example.fake.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "blockchain_products")
public class BlockchainProduct {
	
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String productHash;
	    private String blockNumber;
	    private String transactionHash;

	    private LocalDateTime enrolledAt;

	    @OneToOne
	    @JoinColumn(name = "product_id")
	    private Product product;

	    @PrePersist
	    public void onCreate() {
	        this.enrolledAt = LocalDateTime.now();
	    }

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getProductHash() {
			return productHash;
		}

		public void setProductHash(String productHash) {
			this.productHash = productHash;
		}

		public String getBlockNumber() {
			return blockNumber;
		}

		public void setBlockNumber(String blockNumber) {
			this.blockNumber = blockNumber;
		}

		public String getTransactionHash() {
			return transactionHash;
		}

		public void setTransactionHash(String transactionHash) {
			this.transactionHash = transactionHash;
		}

		public LocalDateTime getEnrolledAt() {
			return enrolledAt;
		}

		public void setEnrolledAt(LocalDateTime enrolledAt) {
			this.enrolledAt = enrolledAt;
		}

		public Product getProduct() {
			return product;
		}

		public void setProduct(Product product) {
			this.product = product;
		}
	    
	    
	    

}
