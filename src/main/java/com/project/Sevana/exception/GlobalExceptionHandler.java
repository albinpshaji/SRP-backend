package com.project.Sevana.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<String> handlenotfound(EntityNotFoundException e){
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("this entity was not found");
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<String> handleaccess(AccessDeniedException e){
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("access to this is denied");
	}
	
	@ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralError(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             	.body("An internal error occurred: ");
    }
	
	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<String> handleNullPointer(NullPointerException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                         	.body("Backend Error: Suggested NGO data or Image was missing. ");
	}
}
