package com.project.Sevana.config.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.Sevana.service.GeocodingService;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    private final GeocodingService geocodingService;

    @Autowired
    public LocationController(GeocodingService geocodingService) {
        this.geocodingService = geocodingService;
    }

    @GetMapping("/search")
    public ResponseEntity<String> searchLocation(@RequestParam String q) {
        if (q == null || q.trim().length() < 3) {
            return ResponseEntity.badRequest().body("[]");
        }
        
        String results = geocodingService.searchLocation(q);
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(results != null ? results : "[]");
    }
}
