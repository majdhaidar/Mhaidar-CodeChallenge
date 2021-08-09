package com.example.demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceController {

	@RequestMapping("/connected")
	public String connected() {
		return "Device Successfully Connected.";
	}
	
	@RequestMapping("/_error")
	public String error() {
		return "Bluetooth Not Enabled On Machine";
	}
}
