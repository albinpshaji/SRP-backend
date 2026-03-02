package com.project.Sevana.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="feedback")
public class Feedback {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long feedbackId;

	@ManyToOne
	@JoinColumn(name="user_id", nullable=false)
	private Users user;

	@Column(nullable=false)
	private String subject;

	@Column(length=2000, nullable=false)
	private String message;

	@Column(nullable=false)
	private String status = "OPEN"; // OPEN or RESOLVED

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt = LocalDateTime.now();
}
