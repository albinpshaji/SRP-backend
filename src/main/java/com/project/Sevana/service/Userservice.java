package com.project.Sevana.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

	public Users registeruser(Users user) {
		return repo.save(user);
	}

}
