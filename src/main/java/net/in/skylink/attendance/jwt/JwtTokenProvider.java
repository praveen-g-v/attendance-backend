package net.in.skylink.attendance.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {
    @Value("${app.jwt.secret}")
    private String jwtSecret;
    
    @Value("${app.jwt.access-token-expiration-ms}")
    private long jwtAccessTokenExpirationMs;
    
    @Value("${app.jwt.refresh-token-expiration-ms}")
    private long jwtRefreshTokenExpirationMs;
    
    @Value("${app.jwt.access-token-cookie-name}")
    private String jwtAccessTokenCookieName;
    
    @Value("${app.jwt.refresh-token-cookie-name}")
    private String jwtRefreshTokenCookieName;

    private Key key() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateAccessToken(String email) {
        return generateTokenFromEmail(email, jwtAccessTokenExpirationMs);
    }
    
    public String generateRefreshToken(String email) {
        return generateTokenFromEmail(email, jwtRefreshTokenExpirationMs);
    }
    
    public String generateToken(UserDetails userDetails, long expirationMs) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", userDetails.getUsername());
        claims.put("authorities", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + expirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    private String generateTokenFromEmail(String email, long expirationMs) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + expirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException | ExpiredJwtException | 
                UnsupportedJwtException | IllegalArgumentException e) {
            return false;
        }
    }
    
    public ResponseCookie generateAccessTokenCookie(String token) {
        return generateCookie(jwtAccessTokenCookieName, token, "/api");
    }
    
    public ResponseCookie generateRefreshTokenCookie(String token) {
        return generateCookie(jwtRefreshTokenCookieName, token, "/api/auth/refresh-token");
    }
    
    public ResponseCookie getCleanAccessTokenCookie() {
        return ResponseCookie.from(jwtAccessTokenCookieName, "").path("/api").build();
    }
    
    public ResponseCookie getCleanRefreshTokenCookie() {
        return ResponseCookie.from(jwtRefreshTokenCookieName, "").path("/api/auth/refresh-token").build();
    }
    
    public String getJwtAccessTokenCookieName() {
        return jwtAccessTokenCookieName;
    }
    
    public String getJwtRefreshTokenCookieName() {
        return jwtRefreshTokenCookieName;
    }
    
    public long getExpirationTime() {
        return jwtAccessTokenExpirationMs;
    }
    
    public long getRefreshTokenExpirationTime() {
        return jwtRefreshTokenExpirationMs;
    }

    private ResponseCookie generateCookie(String name, String value, String path) {
        return ResponseCookie.from(name, value)
                .path(path)
                .maxAge(24 * 60 * 60)
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .build();
    }
}