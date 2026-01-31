package net.in.skylink.attendance.model;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "zoho_oauth_token",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "id")
    }
)
public class ZohoOAuthToken {

    @Id
    private Long id = 1L; // 👈 ALWAYS 1

    @Column(name = "access_token", length = 2000, nullable = false)
    private String accessToken;

    @Column(name = "refresh_token", length = 2000, nullable = false)
    private String refreshToken;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name="client_id",nullable=false)
    private String clientId;
    
    @Column(name="client_secret",nullable=false)
    private String clientSecret;
    

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public LocalDateTime getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(LocalDateTime expiresAt) {
		this.expiresAt = expiresAt;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Long getId() {
		return id;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	
	    
    
    
}

