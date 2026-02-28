package com.project.Sevana.config.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.Sevana.model.Feedback;
import com.project.Sevana.service.Feedbackservice;

@RestController
@RequestMapping("/feedback")
public class Feedbackcontroller {

	private final Feedbackservice service;

	@Autowired
	public Feedbackcontroller(Feedbackservice service) {
		this.service = service;
	}

	@PostMapping
	@PreAuthorize("hasAnyAuthority('DONOR', 'NGO')")
	public ResponseEntity<String> submitFeedback(@RequestBody Map<String, String> payload) {
		try {
			String subject = payload.get("subject");
			String message = payload.get("message");
			if (subject == null || message == null) {
				return ResponseEntity.badRequest().body("Error: Subject and message are required.");
			}
			
			String response = service.submitFeedback(subject, message);
			if (response.startsWith("Error")) {
				return ResponseEntity.badRequest().body(response);
			}
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
		}
	}

	@GetMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public List<Feedback> getAllFeedback() {
		return service.getAllFeedback();
	}

	@PutMapping("/{id}/resolve")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<String> resolveFeedback(@PathVariable Long id) {
		try {
			String response = service.resolveFeedback(id);
			if (response.startsWith("Error")) {
				return ResponseEntity.badRequest().body(response);
			}
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
		}
	}
}
