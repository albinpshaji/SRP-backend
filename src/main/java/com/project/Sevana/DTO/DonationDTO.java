package com.project.Sevana.DTO;


import lombok.Data;

@Data
public class DonationDTO {
	private String username;
	private String title;
	private String description;
	private String status;
	private LogisticsDTO logistics;
	private String category;
	private Long recepientid;
	
	// Fields for Requirements fulfillment
	private Long requirementid;
	private Integer quantityProvided;
	private String imagename;
	private String imagetype;
	private byte[] imagedata;
}
