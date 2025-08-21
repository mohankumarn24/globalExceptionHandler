package net.projectsync.globalexceptionhandler.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Book {

	private Long id;
	private String title;
	private BigDecimal price;
	private LocalDate publishDate;

	// for JPA only, no use
	public Book() {
		
	}

	// getters, setters and constructor
}
