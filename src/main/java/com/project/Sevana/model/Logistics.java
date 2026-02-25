
package com.project.Sevana.model;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="logistics")
public class Logistics {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long logisticsid;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name="donationid")
    private Donations donation;

    @Column(nullable=false)
    private String method; // self drop or pick up

    private LocalDateTime pickupdate;

    private String deliverystatus = "PENDING"; //PENDING ,ACCEPTED,IN_TRANSIT,DELIVERED

    private String addressLine;

    private String proofImageUrl;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="updated_by")
    private Users updatedBy; 

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}