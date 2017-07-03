package com.kris.projects.banktransactions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring boot class containing main function - starting point
 * 
 * @author Krishna Angeras
 *
 */
@SpringBootApplication

public class Application {

	/**
	 * Starting point of the application
	 * 
	 * @param args
	 *            args
	 * @throws Exception
	 *             Exception
	 */
	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}
}