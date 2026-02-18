package com.project.Sevana.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
	Users getAuthenticatedUser() {
		String username= SecurityContextHolder.getContext().getAuthentication().getName();
		return userrepo.findByUsername(username);
	}
	
	public String givedirectdonation(DonationDTO data,MultipartFile imagefile) throws IOException {
		Users donor = getAuthenticatedUser();
		if(data.getRecepientid()==null) {//this is for the market place
			Donations donation = new Donations();
			donation.setTitle(data.getTitle());
			donation.setCategory(data.getCategory());
			donation.setDescription(data.getDescription());
			donation.setPickupLocation(data.getPickuplocation());
			donation.setStatus("PENDING");
			donation.setLogistics(data.getLogistics());
			donation.setImagetype(imagefile.getContentType());
			donation.setImagename(imagefile.getOriginalFilename());
			donation.setImagedata(imagefile.getBytes());
			donation.setDonor(donor);
			donation.setRecipient(null);
			donrepo.save(donation);
			return "success";
		}
		
		Users recepient = userrepo.findById(data.getRecepientid()).orElse(null);
		if(donor==null)
			return "Error:you are not logged in properly";
		if(recepient==null)//this is for if the user sents a fake recepient id
			return "Error:ngo selected not exists";
		if(!recepient.getRole().equals("NGO"))
			return "This is not a ngo";
		Donations donation = new Donations();
		donation.setTitle(data.getTitle());
		donation.setCategory(data.getCategory());
		donation.setDescription(data.getDescription());
		donation.setPickupLocation(data.getPickuplocation());
		donation.setStatus("PENDING");
		donation.setLogistics(data.getLogistics());
		donation.setImagetype(imagefile.getContentType());
		donation.setImagename(imagefile.getOriginalFilename());
		donation.setImagedata(imagefile.getBytes());
		donation.setDonor(donor);
		donation.setRecipient(recepient);
		donrepo.save(donation);
		return "success";
	}

	public List<Users> showallngos(String role1,String role2) {
		return userrepo.findByRoles(role1,role2);
	}
	
	@Transactional(readOnly = true)
	public List<Users> showngos(String role,String keyword) {
		if(keyword!=null) {
			return userrepo.searchbyanything(keyword);
		}
		return userrepo.findByRole(role);
	}
	
	@Transactional(readOnly = true)
	public List<Donations> getmydonations() {
		Long uid = getAuthenticatedUser().getUserid();
		return donrepo.findByDonorUserid(uid);
	}
	
	@Transactional(readOnly = true)
	public List<Donations> getincomingdonations() {
		String username = getAuthenticatedUser().getUsername();
		return donrepo.findByRecipientUsername(username);
	}
	
	@Transactional
	public String acceptdonations(Long id,String status) {
		Long nid = getAuthenticatedUser().getUserid();
		Donations dono = donrepo.getCorrectNgo(id,nid);
		
		if(dono==null) {
			return"retry";
		}
		
		try {
			donrepo.updatestatus(id,status);
			return "success";
		}
		catch(Exception e) {
			return "failure"+e;
		}
	}
	
	@Transactional(readOnly = true)
	public Donations getdonationbyid(long donid) {
		
		Donations don = donrepo.findById(donid).orElse(null);
		
		if(don!=null)
			return don;
		
		return null;
		
	}
	
	@Transactional(readOnly=true)
	public List<Donations> getmarketplacedonations() {
		return donrepo.findformarketplace(); 
	}

	public String claimItem(Long id) {
		Long rid = getAuthenticatedUser().getUserid();
		donrepo.claimitem(id,rid);
		return "donation claiming has been done";
	}

	
	
}
