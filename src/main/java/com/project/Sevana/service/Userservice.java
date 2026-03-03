package com.project.Sevana.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import com.project.Sevana.DTO.UserDTO;
import com.project.Sevana.model.Ngos;
import com.project.Sevana.model.Users;
import com.project.Sevana.repo.Ngosrepo;
import com.project.Sevana.repo.Userrepo;
import com.project.Sevana.service.GeocodingService;

@Service
public class Userservice {
	
	private final Userrepo repo;
	private final AuthenticationManager authmag;
	private final Jwtservice jwtservice;
	private final Ngosrepo nrepo;
	private final GeocodingService geocodingService;
	
	@Autowired
	public Userservice(Userrepo repo,Ngosrepo nrepo,AuthenticationManager authmag,Jwtservice jwtservice,GeocodingService geocodingService) {
		this.repo = repo;
		this.authmag=authmag;
		this.jwtservice=jwtservice;
		this.nrepo = nrepo; 
		this.geocodingService = geocodingService;
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
		
		if (user.getLatitude() != null && user.getLongitude() != null) {
		    GeometryFactory geometryFactory = new GeometryFactory();
		    Point point = geometryFactory.createPoint(new Coordinate(user.getLongitude(), user.getLatitude()));
		    point.setSRID(4326);
		    userdata.setLocationPoint(point);
		    
		    // Server-side reverse geocoding if location name is missing
		    if (userdata.getLocation() == null || userdata.getLocation().trim().isEmpty()) {
		        String resolvedAddress = geocodingService.reverseGeocode(user.getLatitude(), user.getLongitude());
		        if (resolvedAddress != null) {
		            userdata.setLocation(resolvedAddress);
		        }
		    }
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
		user.setLatitude(data.getLatitude());
		user.setLongitude(data.getLongitude());
		
		return user;
	}

	public com.project.Sevana.DTO.LoginresponseDTO completeProfile(UserDTO user, MultipartFile proof, MultipartFile profilepic, jakarta.servlet.http.HttpServletRequest request) throws IOException {
		// Extract username from the JWT token in the request
		String authHeader = request.getHeader("Authorization");
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			throw new RuntimeException("Missing or invalid authorization");
		}
		String token = authHeader.substring(7);
		String username = jwtservice.extractusername(token);
		
		Users existingUser = repo.findByUsername(username);
		if (existingUser == null || !"INCOMPLETE".equals(existingUser.getRole())) {
			throw new RuntimeException("User not found or profile already completed");
		}
		
		// Update common fields
		existingUser.setPhone(user.getPhone());
		existingUser.setLocation(user.getLocation());
		
		// Set location point if coordinates provided
		if (user.getLatitude() != null && user.getLongitude() != null) {
			GeometryFactory geometryFactory = new GeometryFactory();
			Point point = geometryFactory.createPoint(new Coordinate(user.getLongitude(), user.getLatitude()));
			point.setSRID(4326);
			existingUser.setLocationPoint(point);
			
			// Server-side reverse geocoding if location name is missing
			if (existingUser.getLocation() == null || existingUser.getLocation().trim().isEmpty()) {
				String resolvedAddress = geocodingService.reverseGeocode(user.getLatitude(), user.getLongitude());
				if (resolvedAddress != null) {
					existingUser.setLocation(resolvedAddress);
				}
			}
		}
		
		// Set profile picture
		if (profilepic != null && !profilepic.isEmpty()) {
			existingUser.setProfileimagedata(profilepic.getBytes());
			existingUser.setProfileimagename(profilepic.getOriginalFilename());
			existingUser.setProfileimagetype(profilepic.getContentType());
		}
		
		// Handle role-specific logic
		if ("NGO".equals(user.getRole())) {
			existingUser.setRole("NV_NGO"); // Not verified NGO
			if (proof != null && !proof.isEmpty()) {
				Ngos ngo = new Ngos();
				ngo.setLicenceno(user.getLicenceno());
				ngo.setWebsite(user.getWebsite());
				ngo.setProofimagename(proof.getOriginalFilename());
				ngo.setProofimagetype(proof.getContentType());
				ngo.setProofimagedata(proof.getBytes());
				Users savedUser = repo.save(existingUser);
				ngo.setUser(savedUser);
				nrepo.save(ngo);
				
				String newToken = jwtservice.generatetoken(savedUser.getUsername());
				return new com.project.Sevana.DTO.LoginresponseDTO(newToken, savedUser.getRole(), savedUser.getUserid());
			}
			throw new RuntimeException("NGO proof document is required");
		} else {
			// DONOR or any other role
			existingUser.setRole("DONOR");
			Users savedUser = repo.save(existingUser);
			
			String newToken = jwtservice.generatetoken(savedUser.getUsername());
			return new com.project.Sevana.DTO.LoginresponseDTO(newToken, savedUser.getRole(), savedUser.getUserid());
		}
	}

}
