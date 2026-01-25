package com.project.Sevana.config.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.Sevana.DTO.UserDTO;
import com.project.Sevana.model.Ngos;
import com.project.Sevana.model.Users;
import com.project.Sevana.repo.Ngosrepo;
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
	public ResponseEntity<String> verifyuser(@PathVariable Long id,@RequestBody UserDTO data){//Uses the UserDTO here to get data from postman
		String isverify = data.getIsverified();
		String result = service.verifyuser(id,isverify);
		return ResponseEntity.ok(result);
	}
	
	@DeleteMapping("/delete/{did}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String deleteuser(@PathVariable Long did) {
		return service.deleteuser(did);
	}
	
	@GetMapping("/ngodetails/{id}")
	public Ngos ngoDetails(@PathVariable Long id) {
		return service.findngobyId(id);
	}
	
	@GetMapping("/proof/{prid}/image")
	@PreAuthorize("hasAnyAuthority('NGO','ADMIN')")
	public ResponseEntity<?> getimagebyuserid(@PathVariable long prid){
		
			Ngos usr = service.findngobyId(prid);
			if(usr==null) {
				System.out.println("No NGO found with ID: " + prid); 
		        return ResponseEntity.notFound().build();
			}
			
			byte[] image = usr.getProofimagedata();
			if(image ==null) {
				return ResponseEntity.notFound().build();
			}
			
			return ResponseEntity.ok().contentType(MediaType.valueOf(usr.getProofimagetype()))
					.body(image);
		}
	
}
