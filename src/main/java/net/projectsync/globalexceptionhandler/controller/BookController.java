package net.projectsync.globalexceptionhandler.controller;

import java.util.concurrent.TimeUnit;

import org.springframework.http.CacheControl;
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
	
	@ResponseStatus(value = HttpStatus.CREATED)
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
	
    // Cacheable for 1 hour
    @GetMapping("/cacheable-data")
    public ResponseEntity<String> getCacheableData() {
    	
        String body = "This response is cacheable for 1 hour";  // 1 hour: max-age=3600

        HttpHeaders headers = new HttpHeaders();
        // headers.setCacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).getHeaderValue())
        headers.setCacheControl(
        	    CacheControl.maxAge(3600, TimeUnit.SECONDS)
        	                .cachePublic()       // Adds public to the header: public, max-age=3600. public means any cache (browser or proxy) can store this response. 
        	                					 // Without this, Spring treats it as private by default, meaning only the client browser should cache it
        	                .mustRevalidate()    // Adds must-revalidate to the header: public, max-age=10, must-revalidate
        	                					 // This tells caches: once the response becomes stale (after 10 seconds), you must check with the server before reusing it.
        	                					 // Ensures clients don’t serve old data beyond its max age
        	                .getHeaderValue()
        	); // this will produce header --> Cache-Control: public, max-age=3600, must-revalidate
        ResponseEntity<String> entity = new ResponseEntity<String>("A", headers, HttpStatus.OK);
        
        // return ResponseEntity.ok().cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS)).body(body);
        return entity;
    }

    // Not cacheable
    @GetMapping("/non-cacheable-data")
    public ResponseEntity<String> getNonCacheableData() {
    	
        String body = "This response should NOT be cached";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl("no-store, no-cache, must-revalidate"); // Disables caching
        ResponseEntity<String> entity = new ResponseEntity<String>(body, new HttpHeaders(), HttpStatus.OK);
        /**
         * no-store ensures that the response is not stored in any cache.
         * no-cache instructs caches to revalidate the content before serving it.
         * must-revalidate tells caches to check the validity of the cached response with the server before using it
         */

        // return ResponseEntity.ok().cacheControl(CacheControl.noCache()).body(body);
        return entity;
    }
}

/*
| Directive         | Front-End / Browser Behavior                                                                                                                                                                                                                                                 |
| ----------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `public`          | - Any cache (browser, CDN, proxy) can store the response.<br>- The browser itself will also cache it.                                                                                                                                                                        |
| `max-age=10`      | - Cached response is **fresh for 10 seconds**.<br>- Requests within 10 seconds may use cached response without hitting the server.<br>- After 10 seconds, the cache considers the response **stale**.                                                                        |
| `must-revalidate` | - Once the response is stale (after 10 seconds), the browser **must check with the server** before using it.<br>- Browser sends conditional GET (`If-None-Match` / `If-Modified-Since`).<br>- If unchanged, server responds `304 Not Modified` and browser uses cached copy. |
*/