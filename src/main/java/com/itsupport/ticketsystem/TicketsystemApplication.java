package com.itsupport.ticketsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class TicketsystemApplication {

	public static void main(String[] args) {

		SpringApplication.run(TicketsystemApplication.class, args);

		// BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		// String rawPassword = "admin123";  // Change this to your actual password
		// String encodedPassword = encoder.encode(rawPassword);
		// System.out.println("Encoded Password: " + encodedPassword);
	}

}

