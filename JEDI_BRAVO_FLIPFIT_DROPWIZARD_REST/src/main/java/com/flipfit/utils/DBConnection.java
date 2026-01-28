package com.flipfit.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// TODO: Auto-generated Javadoc
/**
 * The Class DBConnection.
 * Utility class for managing database connections
 *
 * @author JEDI-BRAVO
 * @ClassName DBConnection
 */
public class DBConnection {
    
    /** The database URL. */
    private static final String DB_URL = "jdbc:mysql://localhost:3306/Flipfit_schema";
    
    /** The database user. */
    private static final String DB_USER = "root";
    
    /** The database password. */
    private static final String DB_PASSWORD = "";
    
    /** The database driver. */
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    
    /**
     * Private constructor to prevent instantiation.
     */
    private DBConnection() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    /**
     * Gets the database connection.
     *
     * @return the connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName(DB_DRIVER);
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
    }
    
    /**
     * Close connection.
     *
     * @param connection the connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
