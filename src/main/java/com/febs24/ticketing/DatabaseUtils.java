package com.febs24.ticketing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseConnection {
  //Replace placeholders with your actual details
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/febssystem";
    private static final String DATABASE_USER = "root";
    private static final String DATABASE_PASSWORD = "*****";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
        } catch (SQLException e) {
            System.out.println("Failed to get a connection to the database.");
            e.printStackTrace();
            return null;
        }
    }

    public static PreparedStatement prepareStatement(String sql) {
        try {
            Connection connection = getConnection();
            if (connection != null) {
                return connection.prepareStatement(sql);
            } else {
                System.out.println("Failed to prepare statement. No connection to the database.");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Failed to prepare statement.");
            e.printStackTrace();
            return null;
        }
    }
}
