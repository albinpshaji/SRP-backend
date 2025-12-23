package com.project.Sevana.config.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.Sevana.DTO.DonationDTO;
import com.project.Sevana.model.Donations;
import com.project.Sevana.model.Users;
import com.project.Sevana.service.Donationservice;

@RestController
public class Donationcontroller {
	private final Donationservice service;
	@Autowired
	public Donationcontroller(Donationservice service) {
		this.service=service;
	}
	
	@GetMapping("/ngos")
	@PreAuthorize("hasAnyAuthority('ADMIN','DONOR')")
	public List<Users> showallngos(){
		return service.showallngos("NGO");
	}
	
	@PostMapping("/ngos/donate")
	@PreAuthorize("hasAuthority('DONOR')")
	public String givedirectdonation(@RequestBody DonationDTO data) {
		return service.givedirectdonation(data);
	}
	
	@GetMapping("/donations/{id}")
	@PreAuthorize("hasAuthority('DONOR')")
	public List<Donations> getmydonations(){
		return service.getmydonations();
	}
	
	@GetMapping("/incomingdonations")
	@PreAuthorize("hasAuthority('NGO')")
	public List<Donations> getincomingdonations(){
		return service.getincomingdonations();
	}
	
	@PutMapping("/incomingdonations/{id}")
	@PreAuthorize("hasAuthority('NGO')")
	public String acceptdonations(@PathVariable Long id,@RequestBody DonationDTO datastatus) {
		String status = datastatus.getStatus();
		return service.acceptdonations(id,status);
	}
	
}
