package com.project.Sevana.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.Sevana.model.Feedback;
import com.project.Sevana.model.Users;
import com.project.Sevana.repo.Feedbackrepo;
import com.project.Sevana.repo.Userrepo;

@Service
public class Feedbackservice {

	private final Feedbackrepo feedbackrepo;
	private final Userrepo userrepo;

	@Autowired
	public Feedbackservice(Feedbackrepo feedbackrepo, Userrepo userrepo) {
		this.feedbackrepo = feedbackrepo;
		this.userrepo = userrepo;
	}

	private Users getAuthenticatedUser() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		return userrepo.findByUsername(username);
	}

	@Transactional
	public String submitFeedback(String subject, String message) {
		Users user = getAuthenticatedUser();
		if (user == null) {
			return "Error: User not found or not authenticated.";
		}

		Feedback feedback = new Feedback();
		feedback.setUser(user);
		feedback.setSubject(subject);
		feedback.setMessage(message);
		feedback.setStatus("OPEN");
		
		feedbackrepo.save(feedback);
		return "Feedback submitted successfully";
	}

	@Transactional(readOnly = true)
	public List<Feedback> getAllFeedback() {
		return feedbackrepo.findAll();
	}

	@Transactional
	public String resolveFeedback(Long id) {
		Feedback feedback = feedbackrepo.findById(id).orElse(null);
		if (feedback == null) {
			return "Error: Feedback not found";
		}
		
		feedback.setStatus("RESOLVED");
		feedbackrepo.save(feedback);
		return "Feedback resolved successfully";
	}
}
