package net.projectsync.globalexceptionhandler.exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * CustomException extends RTE. This is okay
 * - Use RuntimeException for custom exceptions in Spring Boot apps (business logic, validation, REST errors).
 * - Reserve checked exceptions for cases where the caller really must handle the error right away (e.g., I/O, parsing).
 */

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);    
    
    /**
     * Handler for user built CustomException
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(CustomException ex, HttpServletRequest request) {
        
    	logger.info("custom exception called");
    	/*
    	 * without builder design pattern using POJO ErrorResponse
        ErrorResponsePojo ErrorResponsePojo = new ErrorResponsePojo(
        		LocalDateTime.now()
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase()
                ex.getMessage(),
                request.getRequestURI(),
                
        );
        
         return new ResponseEntity<>(ErrorResponsePojo, HttpStatus.NOT_FOUND);
        */
    	
    	// using builder design pattern
        ErrorResponse errorResponse = ErrorResponse.builder()
        		.timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    
    /**
     * Handler for validation exceptions (e.g., @Valid failed)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        
    	logger.info("MethodArgumentNotValidException called");
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        
        ErrorResponse errorResponse = ErrorResponse.builder()
        		.timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Validation failed")
                .path(errors.toString())
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Handler for incorrect/non-existent endpoints. Ex: /api/v1/<incorrectURI>/1
     * Add below two properties in application.properties file
     *	spring.mvc.throw-exception-if-no-handler-found=true
     *	spring.web.resources.add-mappings=false
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NoHandlerFoundException ex, HttpServletRequest request) {
        
    	logger.info("incorrect endpoint called");
        ErrorResponse errorResponse = ErrorResponse.builder()
        		.timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message("No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL())
                .path(request.getRequestURI())
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    
    // handle wrong HTTP method cases (e.g., sending POST /users/1 when only GET exists).
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        
    	logger.info("incorrect method called");
        ErrorResponse errorResponse = ErrorResponse.builder()
        		.timestamp(LocalDateTime.now())
                .status(HttpStatus.METHOD_NOT_ALLOWED.value())
                .error(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase())
                .message(String.format("Method %s not supported for this endpoint. Supported methods: %s", ex.getMethod(), String.join(", ", ex.getSupportedMethods())))
                .path(request.getRequestURI())
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }
    
    /**
     * Handle all other exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex, HttpServletRequest  request) {
        
    	logger.info("general exception called");
        ErrorResponse errorResponse = ErrorResponse.builder()
        		.timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

/*
@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

  @ResponseStatus(HttpStatus.NOT_FOUND) // 404
  @ExceptionHandler(AccountNotFoundException.class)
  public void handleNotFound(AccountNotFoundException ex) {
    log.error("Requested account not found");
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
  @ExceptionHandler(InvalidAccountRequestException.class)
  public void handleBadRequest(InvalidAccountRequestException ex) {
    log.error("Invalid account supplied in request");
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500
  @ExceptionHandler(Exception.class)
  public void handleGeneralError(Exception ex) {
    log.error("An error occurred processing request" + ex);
  }
}
 */
