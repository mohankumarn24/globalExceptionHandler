package net.projectsync.globalexceptionhandler.exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);    
    
    /**
     * Handler for user built CustomException
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(CustomException ex, WebRequest request) {
        
    	logger.info("custom exception called");
    	/*
    	 * without builder design pattern using POJO ErrorResponse
        ErrorResponsePojo ErrorResponsePojo = new ErrorResponsePojo(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getDescription(false),
                LocalDateTime.now()
        );
        
         return new ResponseEntity<>(ErrorResponsePojo, HttpStatus.NOT_FOUND);
        */
    	
    	// using builder design pattern
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .details(request.getDescription(false))
                .timestamp(LocalDateTime.now())
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    
    /**
     * Handler for validation exceptions (e.g., @Valid failed)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        
    	logger.info("MethodArgumentNotValidException called");
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Validation failed")
                .details(errors.toString())
                .timestamp(LocalDateTime.now())
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Handle all other exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex, WebRequest request) {
        
    	logger.info("general exception called");
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ex.getMessage())
                .details(request.getDescription(false))
                .timestamp(LocalDateTime.now())
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
