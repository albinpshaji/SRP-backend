package com.project.Sevana.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginresponseDTO {
	private String token;
	private String role;
	private Long userid;
}
