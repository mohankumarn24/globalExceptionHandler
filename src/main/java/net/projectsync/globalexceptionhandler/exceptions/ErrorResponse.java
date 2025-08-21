package net.projectsync.globalexceptionhandler.exceptions;

import java.time.LocalDateTime;

/**
 * Error response DTO for API responses with Builder pattern
 */
class ErrorResponse {
	
	private final LocalDateTime timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;
    
    private ErrorResponse(Builder builder) {
       	this.timestamp = builder.timestamp;
    	this.status = builder.status;
        this.error = builder.error;
        this.message = builder.message;
        this.path = builder.path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public int getStatus() {
        return status;
    }
    
    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    /**
     * Builder class for ErrorResponse
     */
    public static class Builder {
    	
        private LocalDateTime timestamp;
        private int status;
        private String error;
        private String message;
        private String path;

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        
        public Builder status(int status) {
            this.status = status;
            return this;
        }
        
        public Builder error(String error) {
            this.error = error;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public ErrorResponse build() {
            return new ErrorResponse(this);
        }
    }

    /**
     * Static factory method to create a new Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }
}

/**
 * Error response model for API responses
 */
/*
class ErrorResponse {
	
    private int status;
    private String error;
    private String message;
    private String path;
    private LocalDateTime timestamp;

    public ErrorResponse(int status, String message, String details, LocalDateTime timestamp) {
    	
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.timestamp = timestamp;
    }

    // Getters and setters
    // use builder design pattern
}
*/

