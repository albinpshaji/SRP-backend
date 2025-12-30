package com.project.Sevana.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginresponseDTO{//used in authcontroller.java
	private String token;
	private String role;
	private Long userid;
}
