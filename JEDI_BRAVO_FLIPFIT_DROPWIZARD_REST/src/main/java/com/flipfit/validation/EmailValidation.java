package com.flipfit.validation;

import java.util.regex.Pattern;

// TODO: Auto-generated Javadoc
/**
 * The Class EmailValidation.
 * Provides email validation functionality
 *
 * @author JEDI-BRAVO
 * @ClassName EmailValidation
 */
public class EmailValidation {
    
    /** The Constant EMAIL_PATTERN for email validation. */
    private static final String EMAIL_PATTERN = 
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    
    /** The pattern. */
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    
    /**
     * Private constructor to prevent instantiation.
     */
    private EmailValidation() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    /**
     * Validate email.
     *
     * @param email the email to validate
     * @return true, if email is valid
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return pattern.matcher(email).matches();
    }
    
    /**
     * Validate email or throw exception.
     *
     * @param email the email to validate
     * @throws IllegalArgumentException if email is invalid
     */
    public static void validateEmail(String email) {
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
    }
}
