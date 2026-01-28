package com.flipfit.exception;

// TODO: Auto-generated Javadoc
/**
 * The Class UserNotFoundException.
 * Custom exception thrown when a user is not found in the system
 *
 * @author JEDI-BRAVO
 * @ClassName UserNotFoundException
 */
public class UserNotFoundException extends Exception {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /**
     * Instantiates a new user not found exception.
     */
    public UserNotFoundException() {
        super("User not found in the system");
    }
    
    /**
     * Instantiates a new user not found exception.
     *
     * @param message the custom error message
     */
    public UserNotFoundException(String message) {
        super(message);
    }
    
    /**
     * Instantiates a new user not found exception.
     *
     * @param message the custom error message
     * @param cause the cause of the exception
     */
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
