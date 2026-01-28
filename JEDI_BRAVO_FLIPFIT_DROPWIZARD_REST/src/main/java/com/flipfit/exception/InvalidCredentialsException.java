package com.flipfit.exception;

// TODO: Auto-generated Javadoc
/**
 * The Class InvalidCredentialsException.
 * Custom exception thrown when user credentials are invalid
 *
 * @author JEDI-BRAVO
 * @ClassName InvalidCredentialsException
 */
public class InvalidCredentialsException extends Exception {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /**
     * Instantiates a new invalid credentials exception.
     */
    public InvalidCredentialsException() {
        super("Invalid email or password");
    }
    
    /**
     * Instantiates a new invalid credentials exception.
     *
     * @param message the custom error message
     */
    public InvalidCredentialsException(String message) {
        super(message);
    }
    
    /**
     * Instantiates a new invalid credentials exception.
     *
     * @param message the custom error message
     * @param cause the cause of the exception
     */
    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}
