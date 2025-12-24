package com.project.Sevana.config.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.Sevana.DTO.UserDTO;
import com.project.Sevana.service.Adminservice;

@RestController
public class Admincontroller {
	
	private Adminservice service;
	
	@Autowired
	public Admincontroller(Adminservice service) {
		this.service= service;
	}
	
	@PostMapping("/verify/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String verifyuser(@PathVariable Long id,@RequestBody UserDTO data){//Uses the UserDTO here to get data from postman
		Boolean isverify = data.getIsverified();
		return service.verifyuser(id,isverify);
	}
	
	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String deleteuser(@PathVariable Long did) {
		return service.deleteuser(did);
	}
}
