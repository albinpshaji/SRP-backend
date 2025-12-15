package com.project.Sevana.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.Sevana.model.Users;
import com.project.Sevana.repo.Userrepo;

@Service
public class Userservice {
	
	private final Userrepo repo;
	private final AuthenticationManager authmag;
	private final Jwtservice jwtservice;
	@Autowired
	public Userservice(Userrepo repo,AuthenticationManager authmag,Jwtservice jwtservice) {
		this.repo = repo;
		this.authmag=authmag;
		this.jwtservice=jwtservice;
	}
	
	public List<Users> getusers() {
		return repo.findAll();
	}

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
	public Users registeruser(Users user) {
		user.setPassword(encoder.encode(user.getPassword()));//encodes the password before saving into db
		return repo.save(user);
	}

	public String verify(Users user) {
		Authentication authen = authmag.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
		if(authen.isAuthenticated())
			return jwtservice.generatetoken(user.getUsername());
		return "fail";
	}

}
