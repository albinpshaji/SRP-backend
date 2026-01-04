package com.project.Sevana.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.Sevana.model.Users;
import com.project.Sevana.repo.Userrepo;

@Service
public class Adminservice {
	
	private Userrepo repo;
	@Autowired
	public Adminservice(Userrepo repo) {
		this.repo = repo;
	}
	public String verifyuser(Long id,Boolean isverify) {//when someone registers as ngo it is saved as NV_NGO as role,when it is verified it is turned to NGO
		Users ngos = repo.findById(id).orElse(null);
		if(!ngos.getRole().equals("NV_NGO")) {
			return "not a ngo";
		}
		
		if(isverify) {//if isverify is true it verifies the user, makes changes to the role
			try {
				repo.verifyuser(id,isverify);
				return "verified";
			}
			catch(Exception e) {
				return "error"+e;
			}
		}
		else {
			try {
				repo.rejectuser(id, isverify);//if isverify is false it rejects the user, makes changes to the role
				return "rejected";
			}
			catch(Exception e) {
				return "error"+e;
			}
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
}
