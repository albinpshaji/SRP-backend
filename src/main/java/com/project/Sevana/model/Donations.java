package com.project.Sevana.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
	
	private String status;
	
	private String logistics = "DROPOF";
	
	private String category;
	
	private String pickupLocation;
	
	@ManyToOne
	@JoinColumn(name="donor_id")
	private Users donor;
	
	@ManyToOne
	@JoinColumn(name="recipient_id")
	private Users recipient;
	
	@Column(columnDefinition="TEXT")
	private String imageurl;
}
