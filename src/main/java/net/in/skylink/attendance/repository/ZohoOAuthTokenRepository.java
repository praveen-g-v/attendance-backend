package net.in.skylink.attendance.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import net.in.skylink.attendance.model.ZohoOAuthToken;

public interface ZohoOAuthTokenRepository extends JpaRepository<ZohoOAuthToken, Long>{
	
	
	Optional<ZohoOAuthToken> findById(Long Id);
	

}
