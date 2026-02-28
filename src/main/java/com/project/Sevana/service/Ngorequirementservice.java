package com.project.Sevana.service;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.project.Sevana.DTO.RequirementsDTO;
import com.project.Sevana.model.Requirements;
import com.project.Sevana.model.Users;
import com.project.Sevana.repo.Ngorequirementrepo;
import com.project.Sevana.repo.Userrepo;

import org.springframework.transaction.annotation.Transactional;

@Service
public class Ngorequirementservice{
	
	private final Ngorequirementrepo repo;
	private final Userrepo userrepo;
	
	Ngorequirementservice(Ngorequirementrepo repo,Userrepo userrepo){
		this.repo=repo;
		this.userrepo = userrepo;
	}
	
	Users getAuthenticatedUser() {
		String username= SecurityContextHolder.getContext().getAuthentication().getName();
		return userrepo.findByUsername(username);
	}

	@Transactional(readOnly = true)
	public List<RequirementsDTO> getrequirementsofngos() {
		Users user = getAuthenticatedUser();
		List<Requirements> reqs;
		if(user.getRole().equalsIgnoreCase("NGO")) {
			reqs = repo.findByUser_Userid(user.getUserid());
		} else {
			reqs = repo.findAll();
		}
		
		return reqs.stream().map(req -> {
			return new RequirementsDTO(
				req.getRequirementid(),
				req.getTitle(),
				req.getCategory(),
				req.getUrgency().name(),
				req.getQuantity(),
				req.getIsActive(),
				req.getDescription(),
				req.getFulfilledQuantity(),
				req.getCreatedAt(),
				req.getUser().getUserid(),
				req.getUser().getUsername(),
				req.getUser().getIsverified()
			);
		}).collect(java.util.stream.Collectors.toList());
	}

	public Requirements addRequirement(Requirements requirement) {
		Users user = getAuthenticatedUser();
		requirement.setUser(user);
		requirement.setIsActive(true);
		requirement.setCreatedAt(java.time.LocalDateTime.now());
		return repo.save(requirement);
	}
	
	@Transactional
	public String deleteRequirement(Long reqId) {
		Users user = getAuthenticatedUser();
		Requirements req = repo.findById(reqId).orElse(null);
		
		if (req == null) {
			return "Error: Requirement not found";
		}
		
		if (!req.getUser().getUserid().equals(user.getUserid())) {
			return "Error: Unauthorized to delete this requirement";
		}
		
		int fulfilled = req.getFulfilledQuantity() != null ? req.getFulfilledQuantity() : 0;
		
		if (fulfilled > 0) {
			// Soft-delete / Deactivate
			req.setQuantity(fulfilled);
			req.setIsActive(false);
			repo.save(req);
			return "Requirement quantity adjusted to match fulfilled amount and deactivated.";
		} else {
			// Hard-delete since no donations exist (or are accepted)
			repo.delete(req);
			return "Requirement deleted successfully.";
		}
	}
}
