package com.project.Sevana.DTO;
import lombok.Data;

@Data
public class UserDTO {//used in admincontroller.java
	private Long userid;
	private String username;
	private String password;
	private String role;
	private String phone;
	private String location;
	private Boolean isverified;
}
