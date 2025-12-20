package com.project.Sevana.model;

import java.util.Collection;
import java.util.Collections;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
// for Authentication spring security expects a UserDetails object instead of your custom model(Users) class 
public class UserPrincipal implements UserDetails {
	
	private Users user;
	public UserPrincipal(Users user) {//gets user details from myuserdetailsservice
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		return Collections.singleton(new SimpleGrantedAuthority(user.getRole().toUpperCase()));//to get the role for authentication
	}

	@Override
	public @Nullable String getPassword() {
		
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		
		return user.getUsername();
	}

}
