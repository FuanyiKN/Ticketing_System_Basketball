package com.febs24.ticketing.controllers;

import com.febs24.ticketing.DatabaseConnection;
import com.febs24.ticketing.dao.UserDao;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import java.sql.SQLException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.io.IOException;

public class SignUpController {
    @FXML
    private TextField usernameField;

    @FXML
    private TextField fullNameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField emailField;

    @FXML
    private Label errorLabel;

    private UserDao userDao;

    public SignUpController() {
        this.userDao = new UserDao(DatabaseConnection.getConnection());
    }

    @FXML
    public void signUp() {
        String username = usernameField.getText();
        String fullName = fullNameField.getText();
        String password = passwordField.getText();
        String email = emailField.getText();

        try {
            // Call the register method on the userDao
            boolean success = userDao.register(username, fullName, password, email, false);

            if (success) {
                // Registration was successful, proceed to the sign-in screen
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/febs24/ticketing/sign-in.fxml"));
                Parent root = loader.load();

                // Get the current stage
                Stage stage = (Stage) usernameField.getScene().getWindow();

                // Set the scene to the sign-in screen
                stage.setScene(new Scene(root));
            } else {
                // Registration failed, show an error message
                errorLabel.setText("Registration failed. Username or email may already be in use.");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void handleRegisterButtonClick() {
        String username = usernameField.getText();
        String fullName = fullNameField.getText();
        String password = passwordField.getText();
        String email = emailField.getText();

        try {
            boolean success = userDao.register(username, fullName, password, email, false);
            if (!success) {
                showAlert(Alert.AlertType.ERROR, "Error creating account", "An account with the provided username or email already exists.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while creating your account. Please try again.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}