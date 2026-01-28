package com.flipfit.validation;

// TODO: Auto-generated Javadoc
/**
 * The Class PasswordValidation.
 * Provides password validation functionality
 *
 * @author JEDI-BRAVO
 * @ClassName PasswordValidation
 */
public class PasswordValidation {
    
    /** The Constant MIN_LENGTH. */
    private static final int MIN_LENGTH = 6;
    
    /** The Constant MAX_LENGTH. */
    private static final int MAX_LENGTH = 20;
    
    /**
     * Private constructor to prevent instantiation.
     */
    private PasswordValidation() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    /**
     * Validate password.
     *
     * @param password the password to validate
     * @return true, if password is valid
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return false;
        }
        
        // Check length
        if (password.length() < MIN_LENGTH || password.length() > MAX_LENGTH) {
            return false;
        }
        
        // Check for at least one digit
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        
        // Check for at least one letter
        boolean hasLetter = password.chars().anyMatch(Character::isLetter);
        
        return hasDigit && hasLetter;
    }
    
    /**
     * Validate password or throw exception.
     *
     * @param password the password to validate
     * @throws IllegalArgumentException if password is invalid
     */
    public static void validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        
        if (password.length() < MIN_LENGTH) {
            throw new IllegalArgumentException("Password must be at least " + MIN_LENGTH + " characters long");
        }
        
        if (password.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Password must be at most " + MAX_LENGTH + " characters long");
        }
        
        if (!password.chars().anyMatch(Character::isDigit)) {
            throw new IllegalArgumentException("Password must contain at least one digit");
        }
        
        if (!password.chars().anyMatch(Character::isLetter)) {
            throw new IllegalArgumentException("Password must contain at least one letter");
        }
    }
    
    /**
     * Gets the password strength.
     *
     * @param password the password
     * @return the password strength (WEAK, MEDIUM, STRONG)
     */
    public static String getPasswordStrength(String password) {
        if (!isValidPassword(password)) {
            return "INVALID";
        }
        
        int score = 0;
        
        // Length score
        if (password.length() >= 8) score++;
        if (password.length() >= 12) score++;
        
        // Has uppercase
        if (password.chars().anyMatch(Character::isUpperCase)) score++;
        
        // Has lowercase
        if (password.chars().anyMatch(Character::isLowerCase)) score++;
        
        // Has digit
        if (password.chars().anyMatch(Character::isDigit)) score++;
        
        // Has special character
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) score++;
        
        if (score <= 2) return "WEAK";
        if (score <= 4) return "MEDIUM";
        return "STRONG";
    }
}
