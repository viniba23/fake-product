package org.example.fake.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "products")
public class Product {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String productName;
	    private double price;
	    private int quantity;
	    private String manufacturer;
	    private LocalDate manufacturingDate;

	    @Column(length = 1000)
	    private String description;

	    private LocalDateTime createdAt;

	    @OneToMany(
	    	    mappedBy = "product",
	    	    cascade = CascadeType.ALL,
	    	    orphanRemoval = true   // ⭐ REQUIRED
	    	)
	    	private List<ProductImage> images;


	    @PrePersist
	    public void onCreate() {
	        this.createdAt = LocalDateTime.now();
	    }

	    /* getters & setters */

	    public void setImages(List<ProductImage> images) {
	        this.images = images;
	    }

	    public List<ProductImage> getImages() {
	        return images;
	    }

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getProductName() {
			return productName;
		}

		public void setProductName(String productName) {
			this.productName = productName;
		}

		public double getPrice() {
			return price;
		}

		public void setPrice(double price) {
			this.price = price;
		}

		public int getQuantity() {
			return quantity;
		}

		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}

		public String getManufacturer() {
			return manufacturer;
		}

		public void setManufacturer(String manufacturer) {
			this.manufacturer = manufacturer;
		}

		public LocalDate getManufacturingDate() {
			return manufacturingDate;
		}

		public void setManufacturingDate(LocalDate manufacturingDate) {
			this.manufacturingDate = manufacturingDate;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public LocalDateTime getCreatedAt() {
			return createdAt;
		}

		public void setCreatedAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
		}
	    
	    
}
