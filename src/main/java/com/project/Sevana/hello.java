package com.project.Sevana;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class hello {
	@GetMapping("/")
	public String printer(){
		return "printing";
	}
}
