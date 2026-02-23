package com.project.Sevana.DTO;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LogisticsDTO {
    private String method;
    private String address_line;
    private LocalDateTime pickupdate;
    private String deliverystatus;
}