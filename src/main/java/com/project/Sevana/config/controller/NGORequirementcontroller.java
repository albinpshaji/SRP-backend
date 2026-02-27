package com.project.Sevana.config.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.Sevana.DTO.RequirementsDTO;
import com.project.Sevana.model.Requirements;
import com.project.Sevana.service.Ngorequirementservice;

@RestController
public class NGORequirementcontroller {
	
	private final Ngorequirementservice service;
	
	@Autowired
	NGORequirementcontroller(Ngorequirementservice service){
		this.service = service;
	}
	
	@GetMapping("requirements")
	public List<RequirementsDTO> getrequirementsofngos() {
		return service.getrequirementsofngos();
	}

	@PostMapping("requirements")
	public Requirements addRequirement(@RequestBody Requirements requirement) {
		return service.addRequirement(requirement);
	}
}
