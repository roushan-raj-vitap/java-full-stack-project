package com.freelance.server.repository;

import org.springframework.stereotype.Repository;

import com.freelance.server.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{

	boolean existsByEmail(String email);

	User findByEmail(String email);
}
