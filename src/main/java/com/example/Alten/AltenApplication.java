package com.example.Alten;

import com.example.Alten.model.Product;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.UUID;

@SpringBootApplication
public class AltenApplication
{

	public static void main(String[] args)
	{
		SpringApplication.run(AltenApplication.class, args);
		// Create the Logger objectt for this class
		Logger logger = LoggerFactory.getLogger(AltenApplication.class);

		Product shampooing = new Product(); // Use no args constructor
		Product gel = new Product (
				UUID.randomUUID(),
				"ZED",
				"Gel",
				"Gel Yves Rocher, cheveux soyeux",
				"Photo",
				"Soin du corps",
				15.5,
				500,
				"AASSDD55",
				100,
				"IN STOCK",
				8.7,
				LocalDateTime.now(),
				LocalDateTime.now()
				); // Use args constructor

		logger.info("Product " + gel.getDescription());

		/*
		Product gel = new Product ("Loreal"); // Use args constructor
		//Testing the setter
		gel.setName("Yves Rocher");
		String brand = gel.getName();

		logger.info("Product name is: " + brand);
		*/
	}

}
