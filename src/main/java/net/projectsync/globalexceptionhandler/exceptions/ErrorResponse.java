package net.projectsync.globalexceptionhandler.exceptions;

import java.time.LocalDateTime;

/**
 * Error response DTO for API responses with Builder pattern
 */
class ErrorResponse {
	
    private final int status;
    private final String message;
    private final String details;
    private final LocalDateTime timestamp;

    private ErrorResponse(Builder builder) {
        this.status = builder.status;
        this.message = builder.message;
        this.details = builder.details;
        this.timestamp = builder.timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Builder class for ErrorResponse
     */
    public static class Builder {
    	
        private int status;
        private String message;
        private String details;
        private LocalDateTime timestamp;

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder details(String details) {
            this.details = details;
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
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
    private String message;
    private String details;
    private LocalDateTime timestamp;

    public ErrorResponse(int status, String message, String details, LocalDateTime timestamp) {
    	
        this.status = status;
        this.message = message;
        this.details = details;
        this.timestamp = timestamp;
    }

    // Getters and setters
    // use builder design pattern
}
*/

