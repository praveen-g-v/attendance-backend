package net.in.skylink.attendance.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.in.skylink.attendance.model.ApiResponse;
import net.in.skylink.attendance.model.ZohoOAuthToken;
import net.in.skylink.attendance.service.ZohoOAuthService;

@RestController
@RequestMapping("/api/public/zohoauthtoken")
public class ZohoOAuthTokenController {
	
	private ZohoOAuthService zohoOAuthService;
	
	public ZohoOAuthTokenController(ZohoOAuthService zohoOAuthService) {
		this.zohoOAuthService=zohoOAuthService;
	}
	
	
	@PostMapping("/update")
	public ResponseEntity<ApiResponse<String>> saveZohoOAuthToken(@RequestBody ZohoOAuthToken zohoOAuthToken){
		zohoOAuthService.saveZohoOAuthToken(zohoOAuthToken);
		return new ResponseEntity<ApiResponse<String>>( new ApiResponse<String>(true, "success", "Updated Successfully"), HttpStatus.ACCEPTED);
		
	}

}
