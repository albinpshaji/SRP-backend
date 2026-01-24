package com.project.Sevana.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ngos {
	
	@Id
	private Long userid;
	
	@OneToOne
	@MapsId
	@JoinColumn(name ="userid")
	private Users user;
	
	private String licenceno;
	
    private String website;
    
    @JsonIgnore
    private String proofimagetype;
    
    @JsonIgnore
    private String proofimagename;
    
    @Lob
    @Column(name = "proofimagedata")
    @JsonIgnore
    private byte[] proofimagedata;
}
