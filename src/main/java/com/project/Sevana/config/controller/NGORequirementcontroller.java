package com.project.Sevana.config.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.project.Sevana.DTO.RequirementsDTO;
import com.project.Sevana.model.Requirements;
import com.project.Sevana.service.Ngorequirementservice;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
public class NGORequirementcontroller {
	
	private final Ngorequirementservice service;
	
	@Autowired
	NGORequirementcontroller(Ngorequirementservice service){
		this.service = service;
	}
	
	@GetMapping("requirements")
	@PreAuthorize("hasAnyAuthority('DONOR', 'NGO', 'ADMIN')")
	public List<RequirementsDTO> getrequirementsofngos() {
		return service.getrequirementsofngos();
	}

	@PostMapping("requirements")
	@PreAuthorize("hasAuthority('NGO')")
	public Requirements addRequirement(@RequestBody Requirements requirement) {
		return service.addRequirement(requirement);
	}
	
	@DeleteMapping("requirements/{reqId}")
	@PreAuthorize("hasAuthority('NGO')")
	public ResponseEntity<String> deleteRequirement(@PathVariable Long reqId) {
		try {
			String response = service.deleteRequirement(reqId);
			if (response.startsWith("Error")) {
				return ResponseEntity.badRequest().body(response);
			}
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
		}
	}
}
