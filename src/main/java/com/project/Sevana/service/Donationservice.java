package com.project.Sevana.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.project.Sevana.DTO.DonationDTO;
import com.project.Sevana.model.Donations;
import com.project.Sevana.model.Users;
import com.project.Sevana.repo.Donationrepo;
import com.project.Sevana.repo.Userrepo;

@Service
public class Donationservice {
	private final Donationrepo donrepo;
	private final Userrepo userrepo;
	@Autowired
	public Donationservice(Donationrepo donrepo,Userrepo userrepo){
		this.donrepo=donrepo;
		this.userrepo=userrepo;
	}
	
	//to get the authenticateduser from jwtitself(stored in memory) used so that a user cant impersonate as an another person
	private Users getAuthenticatedUser() {
		String username= SecurityContextHolder.getContext().getAuthentication().getName();
		return userrepo.findByUsername(username);
	}
	
	public String givedirectdonation(DonationDTO data) {
		Users donor = getAuthenticatedUser();
		Users recepient = userrepo.findById(data.getRecepientid()).orElse(null);
		if(donor==null)
			return "Error:you are not logged in properly";
		if(recepient==null)
			return "Error:ngo selected not exists";
		Donations donation = new Donations();
		donation.setTitle(data.getTitle());
		donation.setCategory(data.getCategory());
		donation.setDescription(data.getDescription());
		donation.setPickupLocation(data.getPickuplocation());
		donation.setStatus(data.getStatus());
		donation.setDonor(donor);
		donation.setRecipient(recepient);
		donrepo.save(donation);
		return "success";
	}

	public List<Users> showallngos(String role) {
		return userrepo.findByRole(role);
	}

	public List<Donations> getmydonations() {
		Long uid = getAuthenticatedUser().getUserid();
		return donrepo.findByDonorUserid(uid);
	}

	public List<Donations> getincomingdonations() {
		String username = getAuthenticatedUser().getUsername();
		return donrepo.findByRecipientUsername(username);
	}

}
