package com.febs24.ticketing.models;


import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

public class User {

    private int userId;
    private String fullName;
    private String username;
    private String hashedPassword;
    private String email;
    private boolean isAdmin;

    // Constructor for existing user data retrieval
    public User(int userId, String fullName, String username, String hashedPassword, String email, boolean isAdmin) {
        this.userId = userId;
        this.fullName = fullName;
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.email = email;
        this.isAdmin = isAdmin;
    }

    // Constructor for new user registration without isAdmin field
    public User(String username, String fullName, String password, String email) {
        this.username = username;
        this.fullName = fullName;
        this.hashedPassword = hashPassword(password);
        this.email = email;
        this.isAdmin = false;
    }

    // Constructor for new user registration
    public User(String username, String fullName, String password, String email, boolean isAdmin) {
        this.username = username;
        this.fullName = fullName;
        this.hashedPassword = hashPassword(password);
        this.email = email;
        this.isAdmin = false;
    }

    // Constructor for updating user profile
    public User(int userId, String username, String fullName, String email) {
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
    }


    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    // Method to check if user is admin
    public boolean checkAdmin() {
        return this.isAdmin;
    }

    // Method to hash password
      public String hashPassword(String password) {
            if (password == null) {
                return null;
            }
            return BCrypt.hashpw(password, BCrypt.gensalt());
        }

    // Method to get hashed password
    public String getHashedPassword() {
            return hashedPassword;
        }

    //FromResultSet method
    public static User fromResultSet(ResultSet rs) throws SQLException {
        int userId = rs.getInt("user_id");
        String fullName = rs.getString("full_name");
        String username = rs.getString("username");
        String hashedPassword = rs.getString("password");
        String email = rs.getString("email");
        boolean isAdmin = rs.getBoolean("is_admin");
        return new User(userId, fullName, username, hashedPassword, email, isAdmin);
    }

    //toString method
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", fullName='" + fullName + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }
}
