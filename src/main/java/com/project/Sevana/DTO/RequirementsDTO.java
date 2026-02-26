package com.project.Sevana.DTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequirementsDTO {
    private Long requirementid;
    private String title;
    private String category;
    private String urgency;
    private Integer quantity;
    private Boolean isActive;
    private String description;
    private Integer fulfilledQuantity;
    private LocalDateTime createdAt;
    
    private Long userid;
    private String username;
    private String isverified;
}
