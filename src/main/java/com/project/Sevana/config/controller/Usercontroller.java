package com.project.Sevana.config.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.Sevana.model.Users;
import com.project.Sevana.service.Userservice;

@RestController
public class Usercontroller {
	
	private final Userservice service;
	
	@Autowired
	public Usercontroller(Userservice service) {
		this.service=service;
	}
	
	
	@GetMapping("/users")
	@PreAuthorize("hasAuthority('ADMIN')")
	public List<Users> getusers() {
		return service.getusers();
	}
	
	
	
}
