package com.project.Sevana.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor 
@AllArgsConstructor
@Table(name ="users")
public class Users {
	public Users(Long userid, String username, String password, String role, String phone, String location, String isverified) {
	    this.userid = userid;
	    this.username = username;
	    this.password = password;
	    this.role = role;
	    this.phone = phone;
	    this.location = location;
	    this.isverified = isverified;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userid;
	
	@Column(unique = true, nullable =false)
	private String username;
	
	@JsonProperty(access=JsonProperty.Access.WRITE_ONLY)//so that password is not send to frontend
	@Column(nullable = false)
	private String password;
	
	@Column(nullable=false)
	private String role;
	
	@Column(nullable=false)
	private String phone;
	
	private String location;
	
	private String isverified="PENDING";
	
	@Column(updatable = false)
	private LocalDateTime createdAt = LocalDateTime.now();
	
	@JsonIgnore
    private String profileimagetype;
    
    @JsonIgnore
    private String profileimagename;
    
    @Lob
    @Column(name = "profileimagedata")
    @JsonIgnore
    @Basic(fetch = FetchType.LAZY) //Dont fetch image from DB unless asked
    @ToString.Exclude //Stop Lombok from printing this in logs
    @EqualsAndHashCode.Exclude //Stop Lombok from using this in comparisons
    private byte[] profileimagedata;
	
}
