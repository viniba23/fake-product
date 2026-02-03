package org.example.fake.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import org.example.fake.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long>{
	Admin findByEmail(String email);
    Admin findByEmailAndPassword(String email, String password);

}
