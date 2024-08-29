package com.febs24.ticketing.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.febs24.ticketing.models.User;
import org.mindrot.jbcrypt.BCrypt;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;

public class UserDao {

    private Connection connection;

    public UserDao(Connection connection) {
        this.connection = connection;
    }

    private String hashPassword(String password) {
        if (password == null) {
            return null;
        }
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean userExists(String username, String email) throws SQLException {
        String sql = "SELECT * FROM Users WHERE username = ? OR email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, email);

            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    public boolean register(String username, String fullName, String password, String email, boolean isAdmin) throws SQLException {
        if (userExists(username, email)) {
            return false;
        }

        String sql = "INSERT INTO Users (username, full_name, password, email, is_admin) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, fullName);
            stmt.setString(3, this.hashPassword(password));
            stmt.setString(4, email);
            stmt.setBoolean(5, isAdmin);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public User login(String username, String password) throws SQLException {
        String sql = "SELECT user_id, full_name, username, password, email, is_admin FROM Users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String retrievedUsername = rs.getString("username");
                String hashedPassword = rs.getString("password");

                // Debug print statements
                System.out.println("Retrieved Username: " + retrievedUsername);
                System.out.println("Retrieved Hashed Password: " + hashedPassword);

                boolean passwordsMatch = BCrypt.checkpw(password, hashedPassword);
                System.out.println("Passwords match: " + passwordsMatch);

                if (passwordsMatch) {
                    return new User(
                            rs.getInt("user_id"),
                            rs.getString("full_name"),
                            retrievedUsername,
                            hashedPassword,
                            rs.getString("email"),
                            rs.getBoolean("is_admin")
                    );
                }
            }
        }
        return null;
    }

    public User getUserByUsername(String username) throws SQLException {
        String sql = "SELECT user_id, full_name, username, email, is_admin FROM Users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("user_id"),
                        rs.getString("full_name"),
                        username,
                        null,
                        rs.getString("email"),
                        rs.getBoolean("is_admin")
                );
            }
        }
        return null;
    }

    public User getUserById(int userId) throws SQLException {
        String sql = "SELECT full_name, username, email, is_admin FROM Users WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        userId,
                        rs.getString("full_name"),
                        rs.getString("username"),
                        null,
                        rs.getString("email"),
                        rs.getBoolean("is_admin")
                );
            }
        }
        return null;
    }

    //Get all users
    public List<User> getAllUsers() throws SQLException {
        String sql = "SELECT * FROM Users";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            users.add(User.fromResultSet(rs));
        }
        return users;
    }

    public boolean updateUserProfile(User user, Connection connection) throws SQLException {
        String sql = "UPDATE Users SET username = ?, full_name = ?, email = ? WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getFullName());
            stmt.setString(3, user.getEmail());
            stmt.setInt(4, user.getUserId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deleteUser(int userId, Connection connection) throws SQLException {
        String sql = "DELETE FROM Users WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    //Make user an admin
    public boolean makeAdmin(int userId, Connection connection) throws SQLException {
        String sql = "UPDATE Users SET is_admin = 1 WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}

