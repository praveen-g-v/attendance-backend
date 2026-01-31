package net.in.skylink.attendance.service;

import org.springframework.stereotype.Service;

import net.in.skylink.attendance.exceptions.ServiceException;
import net.in.skylink.attendance.model.ZohoOAuthToken;
import net.in.skylink.attendance.repository.ZohoOAuthTokenRepository;

@Service
public class ZohoOAuthService {
	
	private ZohoOAuthTokenRepository zohoOAuthTokenRepository;
	
	public ZohoOAuthService(ZohoOAuthTokenRepository zohoOAuthTokenRepository) {
		this.zohoOAuthTokenRepository=zohoOAuthTokenRepository;
	}
	
	
	public ZohoOAuthToken saveZohoOAuthToken(ZohoOAuthToken zohoOAuthToken) {
		ZohoOAuthToken singleToken=this.zohoOAuthTokenRepository.findById(1L).orElse(this.zohoOAuthTokenRepository.save(zohoOAuthToken));
		if(zohoOAuthToken.getAccessToken()!=null||zohoOAuthToken.getAccessToken()!="") {
			singleToken.setAccessToken(zohoOAuthToken.getAccessToken());
		}
		if (zohoOAuthToken.getRefreshToken()!=null||zohoOAuthToken.getRefreshToken()!="") {
			singleToken.setAccessToken(zohoOAuthToken.getAccessToken());
		}
		return this.zohoOAuthTokenRepository.save(singleToken);
	}
	
	
	public String getAccessToken() throws ServiceException {
		ZohoOAuthToken singleToken=this.zohoOAuthTokenRepository.findById(1L).orElseThrow(()->new ServiceException("Access Token is Not Available"));
		return singleToken.getAccessToken();
	}
	
	
	

}
