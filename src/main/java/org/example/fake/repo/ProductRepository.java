	package org.example.fake.repo;
	
	import java.util.List;
	
	import org.example.fake.model.Product;
	import org.springframework.data.jpa.repository.JpaRepository;
	
	public interface ProductRepository extends JpaRepository<Product, Long> {
		 List<Product> findByQuantityLessThanEqual(int quantity);
	}
