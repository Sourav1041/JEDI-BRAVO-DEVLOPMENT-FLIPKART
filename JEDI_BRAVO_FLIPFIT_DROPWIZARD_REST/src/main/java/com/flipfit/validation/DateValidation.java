package com.flipfit.validation;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

// TODO: Auto-generated Javadoc
/**
 * The Class DateValidation.
 * Provides date validation functionality
 *
 * @author JEDI-BRAVO
 * @ClassName DateValidation
 */
public class DateValidation {
    
    /** The Constant DATE_PATTERN. */
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    
    /** The Constant formatter. */
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
    
    /** The Constant MIN_AGE for gym membership. */
    private static final int MIN_AGE = 16;
    
    /** The Constant MAX_AGE for gym membership. */
    private static final int MAX_AGE = 100;
    
    /**
     * Private constructor to prevent instantiation.
     */
    private DateValidation() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    /**
     * Validate date format.
     *
     * @param dateStr the date string to validate
     * @return true, if date format is valid
     */
    public static boolean isValidDateFormat(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return false;
        }
        
        try {
            LocalDate.parse(dateStr, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    
    /**
     * Validate date of birth.
     *
     * @param dateOfBirth the date of birth
     * @return true, if date of birth is valid
     */
    public static boolean isValidDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            return false;
        }
        
        // Date should not be in future
        if (dateOfBirth.isAfter(LocalDate.now())) {
            return false;
        }
        
        // Calculate age
        int age = Period.between(dateOfBirth, LocalDate.now()).getYears();
        
        // Age should be between MIN_AGE and MAX_AGE
        return age >= MIN_AGE && age <= MAX_AGE;
    }
    
    /**
     * Validate date of birth or throw exception.
     *
     * @param dateOfBirth the date of birth
     * @throws IllegalArgumentException if date of birth is invalid
     */
    public static void validateDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            throw new IllegalArgumentException("Date of birth cannot be null");
        }
        
        if (dateOfBirth.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date of birth cannot be in the future");
        }
        
        int age = Period.between(dateOfBirth, LocalDate.now()).getYears();
        
        if (age < MIN_AGE) {
            throw new IllegalArgumentException("Minimum age requirement is " + MIN_AGE + " years");
        }
        
        if (age > MAX_AGE) {
            throw new IllegalArgumentException("Invalid date of birth");
        }
    }
    
    /**
     * Gets the age.
     *
     * @param dateOfBirth the date of birth
     * @return the age in years
     */
    public static int getAge(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            return 0;
        }
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }
    
    /**
     * Validate booking date.
     *
     * @param bookingDate the booking date
     * @return true, if booking date is valid
     */
    public static boolean isValidBookingDate(LocalDate bookingDate) {
        if (bookingDate == null) {
            return false;
        }
        
        // Booking date should not be in the past
        if (bookingDate.isBefore(LocalDate.now())) {
            return false;
        }
        
        // Booking should not be more than 30 days in advance
        LocalDate maxDate = LocalDate.now().plusDays(30);
        return !bookingDate.isAfter(maxDate);
    }
    
    /**
     * Validate booking date or throw exception.
     *
     * @param bookingDate the booking date
     * @throws IllegalArgumentException if booking date is invalid
     */
    public static void validateBookingDate(LocalDate bookingDate) {
        if (bookingDate == null) {
            throw new IllegalArgumentException("Booking date cannot be null");
        }
        
        if (bookingDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Booking date cannot be in the past");
        }
        
        LocalDate maxDate = LocalDate.now().plusDays(30);
        if (bookingDate.isAfter(maxDate)) {
            throw new IllegalArgumentException("Bookings can only be made up to 30 days in advance");
        }
    }
}
