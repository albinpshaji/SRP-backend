package com.project.Sevana.model;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="donations")
public class Donations {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long donationid;
	
	@Column(nullable=false)
	private String title;
	
	private String description;
	
	private String status;//pending,accepted,rejected
	
	@OneToOne(mappedBy = "donation")
    private Logistics logistics;
	
	private String category;
	
	
	@Column(nullable = false)
	private LocalDateTime createdAt = LocalDateTime.now();
	
	@ManyToOne
	@JoinColumn(name="donor_id")
	private Users donor;
	
	@ManyToOne
	@JoinColumn(name="recipient_id")
	private Users recipient;
	
	@ManyToOne
	@JoinColumn(name="requirement_id")
	private Requirements requirement;
	
	private Integer quantityProvided;
	
	@JsonIgnore
	private String imagename;
	
	@JsonIgnore
	private String imagetype;
	
	@Lob 
	@JsonIgnore
	private byte[] imagedata;
}
