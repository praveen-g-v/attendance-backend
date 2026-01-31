package net.in.skylink.attendance.service;

//public class ZohoTokenService {
//
//}

import net.in.skylink.attendance.model.ZohoOAuthToken;
import net.in.skylink.attendance.repository.ZohoOAuthTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class ZohoTokenService {

    private final ZohoOAuthTokenRepository tokenRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    private String tokenUrl="https://accounts.zoho.in/oauth/v2/token";

    public ZohoTokenService(ZohoOAuthTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public String getAccessToken() {

        ZohoOAuthToken token = tokenRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Zoho token not configured"));

        if (token.getExpiresAt().isAfter(LocalDateTime.now().plusMinutes(2))) {
            return token.getAccessToken();
        }

        return refreshAccessToken(token);
    }

    private String refreshAccessToken(ZohoOAuthToken token) {

        String url = tokenUrl +
                "?refresh_token=" + token.getRefreshToken() +
                "&client_id=" + token.getClientId() +
                "&client_secret=" + token.getClientSecret() +
                "&grant_type=refresh_token";

        @SuppressWarnings("unchecked")
        Map<String, Object> response =
                restTemplate.postForObject(url, null, Map.class);
        
        
        System.out.println(response.get("access_token"));

        String newAccessToken = (String) response.get("access_token");
        Integer expiresIn = (Integer) response.get("expires_in");

        token.setAccessToken(newAccessToken);
        token.setExpiresAt(LocalDateTime.now().plusSeconds(expiresIn));

        tokenRepository.save(token);

        return newAccessToken;
    }
}
