package com.flipfit.constants;

/**
 * Database configuration constants for FlipFit application
 */
public class DatabaseConstants {
    // Database connection details
    public static final String DB_URL = "jdbc:mysql://localhost:3306/Flipfit_schema";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "";
    
    // Database driver
    public static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    
    // Private constructor to prevent instantiation
    private DatabaseConstants() {
        throw new UnsupportedOperationException("This is a constants class and cannot be instantiated");
    }
}
