package com.project.Sevana.DTO;

import com.project.Sevana.model.Users;

import lombok.Data;

@Data
public class DonationDTO {
	private String username;
	private String title;
	private String description;
	private String status;
	private String category;
	private String pickuplocation;
	private Long recepientid;
}
