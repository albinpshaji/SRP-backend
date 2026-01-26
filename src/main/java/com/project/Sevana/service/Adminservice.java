package com.project.Sevana.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.Sevana.model.Ngos;
import com.project.Sevana.model.Users;
import com.project.Sevana.repo.Ngosrepo;
import com.project.Sevana.repo.Userrepo;

@Service
public class Adminservice {
	
	private final Userrepo repo;
	private final Ngosrepo nrepo;
	private final Donationservice service;
	@Autowired
	public Adminservice(Userrepo repo,Ngosrepo nrepo,Donationservice service) {
		this.repo = repo;
		this.nrepo = nrepo;
		this.service = service;
	}
	
	
	
	public String verifyuser(Long id,String isverify) {//when someone registers as ngo it is saved as NV_NGO as role,when it is verified it is turned to NGO
		Users ngos = repo.findById(id).orElse(null);
		String role = ngos.getRole();
		if(!(role.equals("NV_NGO") || role.equals("NGO"))) {
			return "not a ngo";
		}
		
		if(isverify.equals("ACCEPTED")) {//if isverify is true it verifies the user, makes changes to the role
			try {
				System.out.println("call has reached");
				repo.verifyuser(id,isverify);
				return "verified";
			}
			catch(Exception e) {
				return "error"+e;
			}
		}
		else if(isverify.equals("REJECTED")){
			try {
				repo.rejectuser(id, isverify);//if isverify is false it rejects the user, makes changes to the role
				return "rejected";
			}
			catch(Exception e) {
				return "error"+e;
			}
		}
		else {
			return "user verification issue";
		}
	}
	
	
	
	public String deleteuser(Long did) {
		Users user=repo.findById(did).orElse(null);
		if(user==null) {
			return "user does not exist";
		}
		try {
			repo.deleteById(did);
			return "success";
		}
		catch(Exception e){
			return "fail"+e;
		}
		
	}
	
	
	@Transactional(readOnly = true)
	public Ngos findngobyId(Long id) {
		Users u = service.getAuthenticatedUser();
		
		if(u.getRole().equals("ADMIN")) {
			return nrepo.findById(id).orElse(null);
		}
		else {
			long uid = u.getUserid();
			return nrepo.findById(uid).orElse(null);	
		}
	}	
}
