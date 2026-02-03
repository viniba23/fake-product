package org.example.fake.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import org.example.fake.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	User findByEmailAndPassword(String email, String password);
    User findByEmail(String email);
}
