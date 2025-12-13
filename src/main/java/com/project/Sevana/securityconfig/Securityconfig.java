package com.project.Sevana.securityconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class Securityconfig {
	@Bean
	public SecurityFilterChain securityfilterchain(HttpSecurity http) throws Exception{
		http.csrf(csrf->csrf.disable());
		http.authorizeHttpRequests(requests->requests.requestMatchers("/users/register/**").
				permitAll().anyRequest().authenticated());
		http.httpBasic(Customizer.withDefaults());
		http.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		return http.build();
		
	}
	
	private UserDetailsService userdetailsservice;
	
	@Autowired
	public Securityconfig(UserDetailsService userdetailsservice) {
		this.userdetailsservice=userdetailsservice;
	}
	
	//used for authentication from database
	@Bean
	public AuthenticationProvider authentication() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userdetailsservice);
		provider.setPasswordEncoder(new BCryptPasswordEncoder(12));// this to convert login request password into hash and compare it with db password
		return provider;
	}
}
