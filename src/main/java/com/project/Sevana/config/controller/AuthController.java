package com.project.Sevana.config.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.Sevana.DTO.LoginresponseDTO;
import com.project.Sevana.DTO.UserDTO;
import com.project.Sevana.model.Users;
import com.project.Sevana.service.GoogleAuthService;
import com.project.Sevana.service.Userservice;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class AuthController {
	private final Userservice service;
	private final GoogleAuthService googleAuthService;
	
	@Autowired
	public AuthController(Userservice service, GoogleAuthService googleAuthService) {
		this.service=service;
		this.googleAuthService=googleAuthService;
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> registeruser(@RequestPart UserDTO user,@RequestPart(required=false) MultipartFile proof,@RequestPart(required=false) MultipartFile profilepic) {
		try {
			Users u = service.registeruser(user,proof,profilepic);
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
	
	@PostMapping("/auth/google")
	public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> body) {
		try {
			String idToken = body.get("idToken");
			LoginresponseDTO response = googleAuthService.authenticateGoogleUser(idToken);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PutMapping("/complete-profile")
	public ResponseEntity<?> completeProfile(
			@RequestPart UserDTO user,
			@RequestPart(required=false) MultipartFile proof,
			@RequestPart(required=false) MultipartFile profilepic,
			HttpServletRequest request) {
		try {
			LoginresponseDTO response = service.completeProfile(user, proof, profilepic, request);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
