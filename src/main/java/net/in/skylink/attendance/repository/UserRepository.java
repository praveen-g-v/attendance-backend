package net.in.skylink.attendance.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.in.skylink.model.User;



	
	@Repository
	public interface UserRepository extends JpaRepository<User,String> {
		Optional<User> findByEmail(String email);
	    Boolean existsByEmail(String email);
//	    Optional<User> findByUsername(String username);
//	    boolean existsByUsername(String username);
	}


