package com.flipfit.exception;

// TODO: Auto-generated Javadoc
/**
 * The Class RegistrationFailedException.
 * Custom exception thrown when user registration fails
 *
 * @author JEDI-BRAVO
 * @ClassName RegistrationFailedException
 */
public class RegistrationFailedException extends Exception {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /**
     * Instantiates a new registration failed exception.
     */
    public RegistrationFailedException() {
        super("Registration failed. Please try again");
    }
    
    /**
     * Instantiates a new registration failed exception.
     *
     * @param message the custom error message
     */
    public RegistrationFailedException(String message) {
        super(message);
    }
    
    /**
     * Instantiates a new registration failed exception.
     *
     * @param message the custom error message
     * @param cause the cause of the exception
     */
    public RegistrationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
