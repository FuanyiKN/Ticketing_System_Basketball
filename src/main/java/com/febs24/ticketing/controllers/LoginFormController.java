package com.febs24.ticketing.controllers;

import com.febs24.ticketing.DatabaseConnection;
import com.febs24.ticketing.dao.BookingDao;
import com.febs24.ticketing.dao.GameDao;
import com.febs24.ticketing.dao.TicketDao;
import com.febs24.ticketing.dao.UserDao;
import com.febs24.ticketing.models.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import java.sql.Connection;
import java.sql.SQLException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.io.IOException;

public class LoginFormController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private UserDao userDao;
    private GameDao gameDao;
    private BookingDao bookingDao;
    private TicketDao ticketDao;

    public LoginFormController() {
        Connection connection = DatabaseConnection.getConnection();
        this.userDao = new UserDao(connection);
        this.gameDao = new GameDao(connection);
        this.bookingDao = new BookingDao(connection);
        this.ticketDao = new TicketDao(connection);
    }

    @FXML
    public void signIn() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            User user = userDao.login(username, password);
            if (user != null) {
                FXMLLoader loader;
                Parent root;
                Stage stage = new Stage();
                if (user.isAdmin()) {
                    loader = new FXMLLoader(getClass().getResource("/com/febs24/ticketing/AdminProfile.fxml"));
                    root = loader.load();
                    AdminProfileController adminProfileController = loader.getController();
                    adminProfileController.setGameDao(gameDao);
                    adminProfileController.setBookingDao(bookingDao);
                    adminProfileController.setUserDao(userDao);
                    adminProfileController.setTicketDao(ticketDao);
                } else {
                    loader = new FXMLLoader(getClass().getResource("/com/febs24/ticketing/UserProfile.fxml"));
                    root = loader.load();
                    UserProfileController userProfileController = loader.getController();
                    userProfileController.setUser(user);
                    userProfileController.setStage(stage); // Set the stage
                }

                stage.setScene(new Scene(root));
                stage.show();

                // Close the sign-in window
                Stage currentStage = (Stage) usernameField.getScene().getWindow();
                currentStage.close();
            } else {
                errorLabel.setText("Invalid username or password");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void signUp() {
        try {
            // Load the registration form
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/febs24/ticketing/SignUp.fxml"));
            Parent root = loader.load();

            // Show the registration form in a new stage
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            // Close the sign-in window
            Stage currentStage = (Stage) usernameField.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}