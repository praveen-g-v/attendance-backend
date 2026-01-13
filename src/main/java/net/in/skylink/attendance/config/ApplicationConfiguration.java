package net.in.skylink.attendance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import net.in.skylink.attendance.repository.UserRepository;
import net.in.skylink.attendance.service.UserService;
import net.in.skylink.attendance.utils.PasswordManager;


@Configuration
public class ApplicationConfiguration {

	
	 	private final UserRepository userRepository;
	    private final UserService userService;

	    public ApplicationConfiguration(UserRepository userRepository, UserService userService) {
	        this.userRepository = userRepository;
	        this.userService = userService;
	    }

	    @Bean
	    public UserDetailsService userDetailsService() {
	        return userService;
	    }
	    @Bean
	    BCryptPasswordEncoder passwordEncoder() {
	        return new PasswordManager(15).getPasswordEncoder();
	    }

	    @Bean
	    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
	        return config.getAuthenticationManager();
	    }

	    @Bean
	    AuthenticationProvider authenticationProvider() {
	        DaoAuthenticationProvider authProvider =  new DaoAuthenticationProvider(userService);
	        authProvider.setPasswordEncoder(passwordEncoder());
	        return authProvider;
	    }
	    
}
