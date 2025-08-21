package net.projectsync.globalexceptionhandler.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import net.projectsync.globalexceptionhandler.model.Book;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public String createBook(@RequestBody Book book) {
		return "Book created successfully";
	}
	
	@GetMapping("/{id}")
	// public ResponseEntity<BookDto> createBook() {
	public ResponseEntity<?> getBookById(@PathVariable int id) {
		
		// additional headers to add
		HttpHeaders headers = new HttpHeaders();
		headers.add("key1", "value1");
		headers.add("key2", "value2");
		
		// return new ResponseEntity<Book>(HttpStatus.OK);
		// return new ResponseEntity<Book>(new Book(), HttpStatus.OK);
		// return new ResponseEntity<Book>(new Book(), headers, HttpStatus.OK);
		// return new ResponseEntity<>(new Book(), headers, HttpStatus.OK);
		
		// using builder option 2
		return ResponseEntity
				.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
		        .header("TEST1","TEST1 VALUE")
		        .header("TEST2", "TEST2 VALUE")
		        // .headers(headers)  					    	// use either individual header or headers object
		        // .body(null);
				// .body("message");							// use .contentType(MediaType.TEXT_PLAIN)
		        .body(new Book());								// use .contentType(MediaType.APPLICATION_JSON)
	}
}