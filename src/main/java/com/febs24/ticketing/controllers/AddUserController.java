package com.febs24.ticketing.controllers;

import com.febs24.ticketing.DatabaseConnection;
import com.febs24.ticketing.dao.UserDao;
import com.febs24.ticketing.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class AddUserController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField fullNameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField emailField;

    @FXML
    private CheckBox isAdminCheckBox;

    private UserDao userDao;

    public AddUserController() {
        this.userDao = new UserDao(DatabaseConnection.getConnection());
    }

    @FXML
    public void submit(ActionEvent event) {
        String username = usernameField.getText();
        String fullName = fullNameField.getText();
        String password = passwordField.getText();
        String email = emailField.getText();
        boolean isAdmin = isAdminCheckBox.isSelected();

        // Save new user to database
        try {
            userDao.register(username, fullName, password, email, isAdmin);

            // Retrieve the user from the database
            User savedUser = userDao.getUserByUsername(username);

            // Use savedUser for further operations
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Close the window
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void cancel(ActionEvent event) {
        // Close the window
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }
}