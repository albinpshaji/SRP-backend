package com.project.Sevana.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.Sevana.model.Users;
import com.project.Sevana.repo.Userrepo;

@Service
public class Userservice {
	
	private final Userrepo repo;
	@Autowired
	public Userservice(Userrepo repo) {
		this.repo = repo;
	}

	public List<Users> getusers() {
		return repo.findAll();
	}

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
	public Users registeruser(Users user) {
		user.setPassword(encoder.encode(user.getPassword()));//encodes the password before saving into db
		return repo.save(user);
	}

}
