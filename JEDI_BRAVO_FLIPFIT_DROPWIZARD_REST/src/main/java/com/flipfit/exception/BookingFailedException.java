package com.flipfit.exception;

// TODO: Auto-generated Javadoc
/**
 * The Class BookingFailedException.
 * Custom exception thrown when a booking operation fails
 *
 * @author JEDI-BRAVO
 * @ClassName BookingFailedException
 */
public class BookingFailedException extends Exception {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /**
     * Instantiates a new booking failed exception.
     */
    public BookingFailedException() {
        super("Booking failed. Unable to process your request");
    }
    
    /**
     * Instantiates a new booking failed exception.
     *
     * @param message the custom error message
     */
    public BookingFailedException(String message) {
        super(message);
    }
    
    /**
     * Instantiates a new booking failed exception.
     *
     * @param message the custom error message
     * @param cause the cause of the exception
     */
    public BookingFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
