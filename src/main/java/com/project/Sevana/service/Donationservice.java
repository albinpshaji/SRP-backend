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
import com.project.Sevana.model.Requirements;
import com.project.Sevana.model.Users;
import com.project.Sevana.repo.Donationrepo;
import com.project.Sevana.repo.Logisticsrepo;
import com.project.Sevana.repo.Ngorequirementrepo;
import com.project.Sevana.repo.Userrepo;

@Service
public class Donationservice {
	private final Donationrepo donrepo;
	private final Userrepo userrepo;
	private final Logisticsrepo logisticsrepo;
	private final Ngorequirementrepo ngorequirementrepo;
	
	@Autowired
	public Donationservice(Donationrepo donrepo,Userrepo userrepo,Logisticsrepo logisticsrepo,Ngorequirementrepo ngorequirementrepo){
		this.donrepo=donrepo;
		this.userrepo=userrepo;
		this.logisticsrepo=logisticsrepo;
		this.ngorequirementrepo=ngorequirementrepo;
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
		
		Users recepient = null;
		Requirements targetRequirement = null;
		
		if (data.getRequirementid() != null) {
			//fulfilling a specific NGO requirement
			targetRequirement = ngorequirementrepo.findById(data.getRequirementid()).orElse(null);
			if (targetRequirement == null) {
				return "Error: requirement not found";
			}
			recepient = targetRequirement.getUser();
		} else {
			//direct donation to NGO without specific requirement
			recepient = userrepo.findById(data.getRecepientid()).orElse(null);
		}
		
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
		
		if (targetRequirement != null) {
			donation.setRequirement(targetRequirement);
			donation.setQuantityProvided(data.getQuantityProvided());
		}
		
		
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
	
	@Transactional(readOnly = true)// here there is a cursor pagination
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
			String oldStatus = dono.getStatus();
			//prevent double donation when some rejects and accepts donation again
			if (oldStatus != null && oldStatus.equalsIgnoreCase(status)) {
				return "success";
			}

			donrepo.updatestatus(id,status);
			
			if ("accepted".equalsIgnoreCase(status)) {
				logisticsrepo.updateDeliveryStatusByDonationId(id, "ACCEPTED");
				
				//handles requirement fulfillment(adding quantitydsgdf)
				if (dono.getRequirement() != null) {
					Requirements req = dono.getRequirement();
					int currentFulfilled = req.getFulfilledQuantity() != null ? req.getFulfilledQuantity() : 0;
					int provided = dono.getQuantityProvided() != null ? dono.getQuantityProvided() : 0;
					
					req.setFulfilledQuantity(currentFulfilled + provided);
					
					if (req.getFulfilledQuantity() >= req.getQuantity()) {
						req.setIsActive(false);
					}
					
					ngorequirementrepo.save(req);
				}
			} else if ("rejected".equalsIgnoreCase(status) || "cancelled".equalsIgnoreCase(status)) {
				//reverts requirement fulfillment if it was previously accepted
				if ("accepted".equalsIgnoreCase(oldStatus) && dono.getRequirement() != null) {
					Requirements req = dono.getRequirement();
					int currentFulfilled = req.getFulfilledQuantity() != null ? req.getFulfilledQuantity() : 0;
					int provided = dono.getQuantityProvided() != null ? dono.getQuantityProvided() : 0;
					
					int newFulfilled = currentFulfilled - provided;
					if (newFulfilled < 0) newFulfilled = 0;
					
					req.setFulfilledQuantity(newFulfilled);
					
					if (req.getFulfilledQuantity() < req.getQuantity()) {
						req.setIsActive(true);
					}
					
					ngorequirementrepo.save(req);
				}
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

	public List<Users> showverifiedngos(String string) {
		return userrepo.findByRole(string);
	}

	@Transactional
	public String deletedonation(Long donid) {
		Long uid = getAuthenticatedUser().getUserid();
		Donations don = donrepo.findById(donid).orElse(null);
		
		if (don == null) {
			return "Error: Donation not found";
		}
		
		if (don.getDonor() == null || !don.getDonor().getUserid().equals(uid)) {
			return "Error: Unauthorized to delete this donation";
		}
		
		//prevents deletion if in transit or delivered
		if (don.getLogistics() != null) {
			String deliverystatus = don.getLogistics().getDeliverystatus();
			if ("IN_TRANSIT".equalsIgnoreCase(deliverystatus) || "DELIVERED".equalsIgnoreCase(deliverystatus)) {
				return "Error: Cannot delete a donation that is already in transit or delivered";
			}
		}
		
		//if accepted revert requirement fulfillment
		if ("ACCEPTED".equalsIgnoreCase(don.getStatus()) && don.getRequirement() != null) {
			Requirements req = don.getRequirement();
			int currentFulfilled = req.getFulfilledQuantity() != null ? req.getFulfilledQuantity() : 0;
			int provided = don.getQuantityProvided() != null ? don.getQuantityProvided() : 0;
			
			int newFulfilled = currentFulfilled - provided;
			if (newFulfilled < 0) newFulfilled = 0;
			
			req.setFulfilledQuantity(newFulfilled);
			
			if (req.getFulfilledQuantity() < req.getQuantity()) {
				req.setIsActive(true);
			}
			
			ngorequirementrepo.save(req);
		}
		
		if (don.getLogistics() != null) {
			logisticsrepo.delete(don.getLogistics());
		}
		
		donrepo.delete(don);
		
		return "success";
	}
	
}
