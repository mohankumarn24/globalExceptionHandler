package net.projectsync.globalexceptionhandler.exceptions;

/**
 * Custom exception for resource not found scenarios
 */
public class CustomException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CustomException(String message) {
        super(message);
    }
}