package net.projectsync.globalexceptionhandler.exceptions;

import java.time.LocalDateTime;

/**
 * Error response model for API responses
 */
class ErrorResponsePojo {
	
    private int status;
    private String message;
    private String details;
    private LocalDateTime timestamp;

    public ErrorResponsePojo(int status, String message, String details, LocalDateTime timestamp) {
    	
        this.status = status;
        this.message = message;
        this.details = details;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
