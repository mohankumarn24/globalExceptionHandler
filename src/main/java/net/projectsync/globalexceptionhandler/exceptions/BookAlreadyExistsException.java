package net.projectsync.globalexceptionhandler.exceptions;

public class BookAlreadyExistsException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public BookAlreadyExistsException(String message){
        super(message);
    }
}