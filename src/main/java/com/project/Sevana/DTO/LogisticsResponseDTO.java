package com.project.Sevana.DTO;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LogisticsResponseDTO {
    private Long logisticsid;
    private Long donationId;
    private String donationTitle;
    private String category;
    private String donorUsername;
    private String method;
    private String addressLine;
    private LocalDateTime pickupdate;
    private String deliverystatus;
    private LocalDateTime createdAt;
    
    //to display who updated it, we can also extract their username if desired
    private String updatedByUsername;
}
