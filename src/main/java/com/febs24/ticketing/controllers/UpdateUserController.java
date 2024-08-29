package com.febs24.ticketing.controllers;

import com.febs24.ticketing.DatabaseConnection;
import com.febs24.ticketing.dao.UserDao;
import com.febs24.ticketing.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class UpdateUserController {

    @FXML
    private TextField userIdField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField fullNameField;

    @FXML
    private TextField emailField;

    private UserDao userDao;

    public UpdateUserController() {
        this.userDao = new UserDao(DatabaseConnection.getConnection());
    }

    @FXML
    public void update(ActionEvent event) {
        int userId = Integer.parseInt(userIdField.getText());
        String username = usernameField.getText();
        String fullName = fullNameField.getText();
        String email = emailField.getText();

        // Create new user object with updated values
        User updatedUser = new User(userId, username, fullName, email);

        // Update user in database
        try {
            userDao.updateUserProfile(updatedUser, DatabaseConnection.getConnection());

            // Retrieve the user from the database
            User savedUser = userDao.getUserById(userId);

            // Use savedUser for further operations
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Close the window
        Stage stage = (Stage) userIdField.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void cancel(ActionEvent event) {
        // Close the window
        Stage stage = (Stage) userIdField.getScene().getWindow();
        stage.close();
    }
}