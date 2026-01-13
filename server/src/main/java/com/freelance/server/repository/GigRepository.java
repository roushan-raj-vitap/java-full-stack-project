package com.freelance.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.freelance.server.model.Gig;

public interface GigRepository extends JpaRepository<Gig,Long>{
	
	List<Gig> findByFreelancerEmail(String email);
}
