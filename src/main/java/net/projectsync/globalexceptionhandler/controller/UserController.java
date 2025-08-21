package net.projectsync.globalexceptionhandler.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import net.projectsync.globalexceptionhandler.exceptions.CustomException;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	@GetMapping
	public String welcome() {
		return "hello, world";				// 200 and hello, world
	}
	
	@GetMapping("/custom-exception")
	public String testCustomException() {
		
    	throw new CustomException("some exception");
    	// throw new BookAlreadyExistsException("some exception");
    	// throw new BookNotFoundException("some exception");
    	// throw new ResourceNotFoundException("some exception");
        // return bookService.findAll();
	}
	
    /**
     * Handler for all other general exceptions
     */
	@GetMapping("/global-exception")
	public String testGlobalException() throws Exception {
		
		throw new NullPointerException(); 	 	// 500 and uses spring default global exception handler
		
		/* 
		// without ControllerAdvice
		{
			"timestamp": "2025-08-21T10:29:27.505+00:00",
			"status": 500,
			"error": "Internal Server Error",
			"path": "/api/v1/users/globalexception"
		}
		
		// with ControllerAdvice
		{
			"status": 500,
			"message": null,
			"details": "uri=/api/v1/users/globalexception",
			"timestamp": "2025-08-21T16:07:42.8747041"
		}
		*/
	}
}
