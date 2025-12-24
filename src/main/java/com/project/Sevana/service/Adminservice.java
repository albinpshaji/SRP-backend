package com.project.Sevana.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.Sevana.repo.Userrepo;

@Service
public class Adminservice {
	
	private Userrepo repo;
	@Autowired
	public Adminservice(Userrepo repo) {
		this.repo = repo;
	}
	public String verifyuser(Long id,Boolean isverify) {
		if(isverify) {
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
				repo.rejectuser(id, isverify);
				return "rejected";
			}
			catch(Exception e) {
				return "error"+e;
			}
		}
	}
	
}
