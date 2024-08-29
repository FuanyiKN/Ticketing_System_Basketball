package com.febs24.ticketing;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionApplication {
    public static void main(String[] args) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        if (connection != null) {
            System.out.println("Successfully connected to the database.");
        } else {
            System.out.println("Failed to connect to the database.");
        }
    }
}
