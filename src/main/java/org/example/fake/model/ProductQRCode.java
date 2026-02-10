package org.example.fake.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "product_qr_codes")
public class ProductQRCode {

	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Lob
	    @Column(columnDefinition = "LONGBLOB")
	    private byte[] qrImage;

	    @OneToOne
	    @JoinColumn(name = "product_id")
	    private Product product;

	    private LocalDateTime createdAt;

	    @PrePersist
	    public void onCreate() {
	        this.createdAt = LocalDateTime.now();
	    }

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public byte[] getQrImage() {
			return qrImage;
		}

		public void setQrImage(byte[] qrImage) {
			this.qrImage = qrImage;
		}

		public Product getProduct() {
			return product;
		}

		public void setProduct(Product product) {
			this.product = product;
		}

		public LocalDateTime getCreatedAt() {
			return createdAt;
		}

		public void setCreatedAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
		}
	    
	    
	    
}
