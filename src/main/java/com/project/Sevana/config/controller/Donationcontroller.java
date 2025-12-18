package com.project.Sevana.config.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
	public List<Users> showallngos(){
		return service.showallngos("NGO");
	}
	
	@PostMapping("/ngos/donate")
	public String givedirectdonation(@RequestBody DonationDTO data) {
		return service.givedirectdonation(data);
	}
	
}
