package com.project.Sevana.config.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.Sevana.DTO.LoginresponseDTO;
import com.project.Sevana.model.Users;
import com.project.Sevana.service.Userservice;

@RestController
public class AuthController {
	private final Userservice service;
	
	@Autowired
	public AuthController(Userservice service) {
		this.service=service;
	}
	
	@PostMapping("/register")
	public Users registeruser(@RequestBody Users user) {
		return service.registeruser(user);
	}
	
	@PostMapping("/login")
	public LoginresponseDTO login(@RequestBody Users user) {
		String token = service.verify(user);
		Users dbuser =service.findbyusername(user.getUsername());
		return new LoginresponseDTO(token,dbuser.getRole(),dbuser.getUserid());
	}
}
