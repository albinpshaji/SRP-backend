package com.project.Sevana.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	public String givedirectdonation(DonationDTO data) {
		Users donor = userrepo.findByUsername(data.getUsername());
		Users recepient = userrepo.findById(data.getRecepientid()).orElse(null);
		if(donor==null || recepient==null)
			return "Error:donor or recepient not found";
		Donations donation = new Donations();
		donation.setTitle(data.getTitle());
		donation.setCategory(data.getCategory());
		donation.setDescription(data.getDescription());
		donation.setPickuplocation(data.getPickuplocation());
		donation.setStatus(data.getStatus());
		donation.setUserid(donor);
		donation.setRecepient(recepient);
		donrepo.save(donation);
		return "success";
	}

	public List<Users> showallngos(String role) {
		
		return userrepo.findByRole(role);
	}

}
