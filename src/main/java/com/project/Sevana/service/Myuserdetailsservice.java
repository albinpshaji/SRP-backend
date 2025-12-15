package com.project.Sevana.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.Sevana.model.UserPrincipal;
import com.project.Sevana.model.Users;
import com.project.Sevana.repo.Userrepo;
// used for authentication
@Service
public class Myuserdetailsservice implements UserDetailsService{
	
	
	private Userrepo repo;
	
	@Autowired
	public Myuserdetailsservice(Userrepo repo) {
		this.repo = repo;
	}
	//this method checks if username from login request exist in db , then extract the details of that user
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Users user = repo.findByUsername(username);
		
		if(user==null)
			throw new UsernameNotFoundException("username not found");
		return new UserPrincipal(user);
	}
	
}
