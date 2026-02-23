package com.project.Sevana.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.project.Sevana.DTO.DonationDTO;
import com.project.Sevana.model.Donations;
import com.project.Sevana.model.Logistics;
import com.project.Sevana.model.Users;
import com.project.Sevana.repo.Donationrepo;
import com.project.Sevana.repo.Logisticsrepo;
import com.project.Sevana.repo.Userrepo;

@Service
public class Donationservice {
	private final Donationrepo donrepo;
	private final Userrepo userrepo;
	private final Logisticsrepo logisticsrepo;
	
	@Autowired
	public Donationservice(Donationrepo donrepo,Userrepo userrepo,Logisticsrepo logisticsrepo){
		this.donrepo=donrepo;
		this.userrepo=userrepo;
		this.logisticsrepo=logisticsrepo;
	}
	
	//to get the authenticateduser from jwtitself(stored in memory) used so that a user cant impersonate as an another person
	Users getAuthenticatedUser() {
		String username= SecurityContextHolder.getContext().getAuthentication().getName();
		return userrepo.findByUsername(username);
	}
	
	@Transactional
	public String givedirectdonation(DonationDTO data,MultipartFile imagefile) throws IOException {
		Users donor = getAuthenticatedUser();
		
		if(data.getRecepientid()==null) {//this is for the market place
			Donations donation = new Donations();
			donation.setTitle(data.getTitle());
			donation.setCategory(data.getCategory());
			donation.setDescription(data.getDescription());
			donation.setStatus("PENDING");
			donation.setImagetype(imagefile.getContentType());
			donation.setImagename(imagefile.getOriginalFilename());
			donation.setImagedata(imagefile.getBytes());
			donation.setDonor(donor);
			donation.setRecipient(null);
			
			
			Donations savedDonation = donrepo.save(donation);
			
			
			if (data.getLogistics() != null) {
				Logistics logistics = new Logistics();
				logistics.setMethod(data.getLogistics().getMethod());
				logistics.setAddressLine(data.getLogistics().getAddress_line());
				logistics.setPickupdate(data.getLogistics().getPickupdate());
				logistics.setDeliverystatus("PENDING");
				logistics.setDonation(savedDonation); 
				
				logisticsrepo.save(logistics); 
			}
			return "success";
		}
		
		Users recepient = userrepo.findById(data.getRecepientid()).orElse(null);
		if(donor==null)
			return "Error:you are not logged in properly";
		if(recepient==null)
			return "Error:ngo selected not exists";
		if(!recepient.getRole().equals("NGO"))
			return "This is not a ngo";
			
		Donations donation = new Donations();
		donation.setTitle(data.getTitle());
		donation.setCategory(data.getCategory());
		donation.setDescription(data.getDescription());
		donation.setStatus("PENDING");
		donation.setImagetype(imagefile.getContentType());
		donation.setImagename(imagefile.getOriginalFilename());
		donation.setImagedata(imagefile.getBytes());
		donation.setDonor(donor);
		donation.setRecipient(recepient);
		
		
		Donations savedDonation = donrepo.save(donation);
		
		
		if (data.getLogistics() != null) {
			Logistics logistics = new Logistics();
			logistics.setMethod(data.getLogistics().getMethod());
			logistics.setAddressLine(data.getLogistics().getAddress_line());
			logistics.setPickupdate(data.getLogistics().getPickupdate());
			logistics.setDeliverystatus("PENDING");
			logistics.setDonation(savedDonation); 
			
			logisticsrepo.save(logistics); 
		}
		
		return "success";
	}

	public List<Users> showallngos(String role1,String role2) {
		return userrepo.findByRoles(role1,role2);
	}
	
	@Transactional(readOnly = true)
	public Map<String,Object> showngos(String keyword, Long lastid, int size) {
		Map<String,Object> res = new HashMap<>();
		List<Users> ngos = userrepo.searchbyanything(keyword, lastid, size);
		
		Long nextCursor = null;
		if(!ngos.isEmpty()) {
			nextCursor = ngos.get(ngos.size()-1).getUserid();
		}
		
		res.put("nextCursor", nextCursor);
		res.put("content", ngos);
		res.put("hasMore", ngos.size()==size);
		
		return res;
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
			
			if ("accepted".equalsIgnoreCase(status)) {
				logisticsrepo.updateDeliveryStatusByDonationId(id, "ACCEPTED");
			}
			
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

	@Transactional
	public String claimItem(Long id) {
		Long rid = getAuthenticatedUser().getUserid();
		donrepo.claimitem(id,rid);
		logisticsrepo.updateDeliveryStatusByDonationId(id, "ACCEPTED");
		return "donation claiming has been done";
	}

	
	
}
