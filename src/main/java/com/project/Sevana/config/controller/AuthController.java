package com.project.Sevana.config.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.Sevana.DTO.LoginresponseDTO;
import com.project.Sevana.DTO.UserDTO;
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
	public ResponseEntity<?> registeruser(@RequestPart UserDTO user,@RequestPart(required=false) MultipartFile imagefile) {
		try {
			Users u = service.registeruser(user,imagefile);
			return new ResponseEntity<>(u,HttpStatus.CREATED);

		}
		catch(Exception e) {
			return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/login")
	public LoginresponseDTO login(@RequestBody Users user) {
		String token = service.verify(user);
		Users dbuser =service.findbyusername(user.getUsername());
		return new LoginresponseDTO(token,dbuser.getRole(),dbuser.getUserid());
	}
}
