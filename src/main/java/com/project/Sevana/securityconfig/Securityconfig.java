package com.project.Sevana.securityconfig;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.project.Sevana.securityFilter.Jwtfilter;

@Configuration
@EnableWebSecurity
public class Securityconfig {

	private final UserDetailsService userdetailsservice;
	private final Jwtfilter jwtfilter;
	
	@Autowired
	public Securityconfig(UserDetailsService userdetailsservice,Jwtfilter jwtfilter) {
		this.userdetailsservice=userdetailsservice;
		this.jwtfilter=jwtfilter;
	}
	
	@Bean
	public SecurityFilterChain securityfilterchain(HttpSecurity http) throws Exception{
		http.csrf(csrf->csrf.disable());
		http.cors(Customizer.withDefaults());
		http.authorizeHttpRequests(requests->requests.requestMatchers("/register/**","/login/**").
				permitAll().anyRequest().authenticated());
		http.httpBasic(Customizer.withDefaults());
		http.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.addFilterBefore(jwtfilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
		
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration confi = new CorsConfiguration();
		confi.setAllowedOrigins(List.of("http://localhost:5173"));
		confi.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		confi.setAllowedHeaders(List.of("Authorization","Content-Type"));
		confi.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", confi);
		return source;
	}
	
	//used for authentication from database
	@Bean
	public AuthenticationProvider authentication() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userdetailsservice);
		provider.setPasswordEncoder(new BCryptPasswordEncoder(12));// this to convert login request password into hash and compare it with db password
		return provider;
	}
	
	@Bean
	public AuthenticationManager Authmag(AuthenticationConfiguration config) throws Exception{
		return config.getAuthenticationManager();
	}
}
