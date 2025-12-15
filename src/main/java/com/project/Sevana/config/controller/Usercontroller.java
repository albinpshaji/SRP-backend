package com.project.Sevana.config.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.Sevana.model.Users;
import com.project.Sevana.service.Userservice;

@RestController
@RequestMapping("/users")
public class Usercontroller {
	
	private final Userservice service;
	
	@Autowired
	public Usercontroller(Userservice service) {
		this.service=service;
	}
	
	
	@GetMapping("/")
	public List<Users> getusers() {
		return service.getusers();
	}
	
	@PostMapping("/register")
	public Users registeruser(@RequestBody Users user) {
		return service.registeruser(user);
	}
	
	@PostMapping("/login")
	public String login(@RequestBody Users user) {
		return service.verify(user);
		
	}
	
}
