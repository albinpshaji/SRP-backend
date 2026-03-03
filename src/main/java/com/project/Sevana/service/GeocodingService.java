package com.project.Sevana.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;


import java.util.Map;

@Service
public class GeocodingService {

    @Value("${nominatim.url:https://nominatim.openstreetmap.org}")
    private String nominatimUrl;

    private final RestTemplate restTemplate;

    public GeocodingService() {
        this.restTemplate = new RestTemplate();
    }

    private HttpEntity<String> buildRequestEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "SevanaApp/1.0");
        return new HttpEntity<>(headers);
    }

    @SuppressWarnings("unchecked")
    public String reverseGeocode(double lat, double lon) {
        try {
            String url = String.format("%s/reverse?format=json&lat=%s&lon=%s", nominatimUrl, lat, lon);
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, buildRequestEntity(), Map.class);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Object displayName = response.getBody().get("display_name");
                if (displayName != null) {
                    return displayName.toString();
                }
            }
        } catch (Exception e) {
            System.err.println("Reverse geocoding failed: " + e.getMessage());
        }
        return null;
    }

    public String searchLocation(String query) {
        try {
            String url = String.format("%s/search?format=json&q=%s&limit=5&countrycodes=in", nominatimUrl, query);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, buildRequestEntity(), String.class);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            }
        } catch (Exception e) {
            System.err.println("Map search failed: " + e.getMessage());
        }
        return "[]";
    }
}
