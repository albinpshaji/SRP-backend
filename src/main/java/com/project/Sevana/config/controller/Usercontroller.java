package com.project.Sevana.config.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.project.Sevana.model.Donations;
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
	
	@GetMapping("/user/{usrid}/image")
	@PreAuthorize("hasAnyAuthority('DONOR','NGO','ADMIN')")
	public ResponseEntity<byte[]> getimagebyuserid(@PathVariable long usrid){
		Users usr = service.getuserbyid(usrid);
		byte[] image = usr.getProfileimagedata();
		if(image ==null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().contentType(MediaType.valueOf(usr.getProfileimagetype()))
				.body(image);
	}
	
	
}
