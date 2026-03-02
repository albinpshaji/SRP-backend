package com.project.Sevana.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.Sevana.model.Feedback;

@Repository
public interface Feedbackrepo extends JpaRepository<Feedback, Long> {
	
}
