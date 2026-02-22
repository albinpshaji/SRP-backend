package com.project.Sevana.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.Sevana.DTO.UserDTO;
import com.project.Sevana.model.Ngos;
import com.project.Sevana.model.Users;
import com.project.Sevana.repo.Ngosrepo;
import com.project.Sevana.repo.Userrepo;

@Service
public class Userservice {
	
	private final Userrepo repo;
	private final AuthenticationManager authmag;
	private final Jwtservice jwtservice;
	private final Ngosrepo nrepo;
	
	@Autowired
	public Userservice(Userrepo repo,Ngosrepo nrepo,AuthenticationManager authmag,Jwtservice jwtservice) {
		this.repo = repo;
		this.authmag=authmag;
		this.jwtservice=jwtservice;
		this.nrepo = nrepo; 
	}
	
	public List<Users> getusers() {
		return repo.findAll();
	}

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
	
	public Users registeruser(UserDTO user,MultipartFile proof,MultipartFile profilepic) throws IOException {//when someone registers as ngo it is saved as NV_NGO as role,when it is verified it is turned to NGO
		Users userdata = new Users();
		userdata.setUsername(user.getUsername());
		userdata.setRole(user.getRole());
		userdata.setPhone(user.getPhone());
		userdata.setLocation(user.getLocation());
		userdata.setIsverified("PENDING");
		userdata.setPassword(encoder.encode(user.getPassword()));//encodes the password before saving into db
		if(profilepic!=null && !profilepic.isEmpty()) {
			userdata.setProfileimagedata(profilepic.getBytes());
			userdata.setProfileimagename(profilepic.getOriginalFilename());
			userdata.setProfileimagetype(profilepic.getContentType());
		}
		
		
		
		if(userdata.getRole().equals("NGO")) {
			userdata.setRole("NV_NGO");//not verified ngo
			if(proof!=null && !proof.isEmpty()) {
				Ngos ngo = new Ngos();
				ngo.setLicenceno(user.getLicenceno());
				ngo.setWebsite(user.getWebsite());
				ngo.setProofimagename(proof.getOriginalFilename());
				ngo.setProofimagetype(proof.getContentType());
				ngo.setProofimagedata(proof.getBytes());
				Users u=repo.save(userdata);
				ngo.setUser(u);
				nrepo.save(ngo);
				return u;
			}
			return null;
		}
		else return repo.save(userdata);
			
		
	}

	public String verify(Users user) {
		Authentication authen = authmag.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
		if(authen.isAuthenticated())
			return jwtservice.generatetoken(user.getUsername());
		return "fail";
	}
	
	public Users findbyusername(String username) {
		return repo.findByUsername(username);
	}

	public Users getuserbyid(long usrid) {
		return repo.findById(usrid).orElse(null);
	}

	public UserDTO getprofiledata(Long userid) {
		Users data = repo.findById(userid).orElse(null);
		UserDTO user = new UserDTO();
		user.setUserid(data.getUserid());
		user.setUsername(data.getUsername());
		user.setPhone(data.getPhone());
		user.setLocation(data.getLocation());
		
		return user;
	}

	

}
