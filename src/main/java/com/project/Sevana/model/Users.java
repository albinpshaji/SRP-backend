package com.project.Sevana.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor 
@AllArgsConstructor
@Table(name ="users")
public class Users {
	
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
	
	private Boolean isverified=false;
}
