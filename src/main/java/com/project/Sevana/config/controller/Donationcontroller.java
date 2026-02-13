package com.project.Sevana.config.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
	
	@GetMapping("/allngos")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public List<Users> showallngos(){
		return service.showallngos("NGO","NV_NGO");
	}
	
	@GetMapping("/ngos")
	@PreAuthorize("hasAnyAuthority('ADMIN','DONOR')")
	public List<Users> showngos(){
		return service.showngos("NGO");
	}
	
	@PostMapping("/ngos/donate")
	@PreAuthorize("hasAuthority('DONOR')")
	public ResponseEntity<?> givedirectdonation(@RequestPart DonationDTO data,@RequestPart MultipartFile imagefile) {
		try {
			String don = service.givedirectdonation(data,imagefile);
			return new ResponseEntity<>(don,HttpStatus.CREATED);
		}
		catch(Exception e) {
			return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@GetMapping("/getmarketplaceitems")
	@PreAuthorize("hasAuthority('NGO')")
	public ResponseEntity<List<Donations>> getmarketplaceitems(){
		List<Donations> donations = service.getmarketplacedonations();
		return ResponseEntity.ok(donations);
	}
	
	
	@GetMapping("/mydonations")
	@PreAuthorize("hasAuthority('DONOR')")
	public List<Donations> getmydonations(){
		return service.getmydonations();
	}
	
	@GetMapping("/mydonations/{donid}")
	@PreAuthorize("hasAnyAuthority('DONOR','NGO')")
	public ResponseEntity<byte[]> getimagebyprodid(@PathVariable long donid){
		Donations don = service.getdonationbyid(donid);
		byte[] image = don.getImagedata();
		if(image ==null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().contentType(MediaType.valueOf(don.getImagetype()))
				.body(image);
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
	
	@PutMapping("/ngos/marketplace/claim/{donid}")
	@PreAuthorize("hasAuthority('NGO')")
	public ResponseEntity<String> claimItem(@PathVariable Long donid){
		String sta = service.claimItem(donid);
		return ResponseEntity.ok("item claimed successfully");
	}
	
}
