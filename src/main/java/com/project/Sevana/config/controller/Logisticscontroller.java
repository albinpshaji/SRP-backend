package com.project.Sevana.config.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.Sevana.DTO.LogisticsDTO;
import com.project.Sevana.model.Logistics;
import com.project.Sevana.service.Logisticsservice;

@RestController
public class Logisticscontroller {

    private final Logisticsservice logisticsservice;

    @Autowired
    public Logisticscontroller(Logisticsservice logisticsservice) {
        this.logisticsservice = logisticsservice;
    }

    
    @GetMapping("/logistics")
    @PreAuthorize("hasAuthority('NGO')")
    public ResponseEntity<List<Logistics>> getLogisticsForNgo() {
        return ResponseEntity.ok(logisticsservice.getLogisticsForNgo());
    }

    @PutMapping("/logistics/{id}")
    @PreAuthorize("hasAuthority('NGO')")
    public ResponseEntity<String> updateDeliveryStatus(@PathVariable Long id, @RequestBody LogisticsDTO dto) {
    	
        String result = logisticsservice.updateDeliveryStatus(id, dto);
        if ("success".equals(result)) {
            return ResponseEntity.ok("Delivery status updated successfully");
        }
        return ResponseEntity.badRequest().body("Logistics record not found");
    }
}
