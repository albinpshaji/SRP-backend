package com.project.Sevana.DTO;


import lombok.Data;

@Data
public class DonationDTO {
	private String username;
	private String title;
	private String description;
	private String status;
	private String logistics;
	private String category;
	private String pickuplocation;
	private Long recepientid;
	private String imageurl;
}
