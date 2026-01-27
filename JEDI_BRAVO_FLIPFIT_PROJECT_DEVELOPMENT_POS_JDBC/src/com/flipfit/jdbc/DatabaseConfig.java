package com.flipfit.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
	private static final String URL = "jdbc:mysql://localhost:3306/JEDI_BRAVO_FLIPFIT_PROJECT_DEVELOPMENT_POS_JDBC";
    private static final String USER = "root";
    private static final String PASSWORD = "yourpassword";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
