package com.febs24.ticketing.controllers;

import com.febs24.ticketing.DatabaseConnection;
import com.febs24.ticketing.dao.BookingDao;
import com.febs24.ticketing.models.Booking;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class AddBookingController {

    @FXML
    private TextField ticketIdField;

    @FXML
    private TextField userIdField;

    @FXML
    private TextField guestEmailField;

    @FXML
    private TextField quantityField;

    @FXML
    private TextField totalPriceField;

    private BookingDao bookingDao;

    public AddBookingController() {
        this.bookingDao = new BookingDao(DatabaseConnection.getConnection());
    }

    @FXML
    public void submit(ActionEvent event) {
        int ticketId = Integer.parseInt(ticketIdField.getText());
        int userId = Integer.parseInt(userIdField.getText());
        String guestEmail = guestEmailField.getText();
        int quantity = Integer.parseInt(quantityField.getText());
        double totalPrice = Double.parseDouble(totalPriceField.getText());

        // Create new booking
        try {
            Booking newBooking = new Booking(ticketId, userId, guestEmail, quantity, totalPrice, DatabaseConnection.getConnection());

            // Save new booking to database
            bookingDao.createBooking(newBooking);

            // Retrieve the booking from the database
            Booking savedBooking = bookingDao.getBooking(newBooking.getBookingId());

            // Use savedBooking for further operations
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Close the window
        Stage stage = (Stage) ticketIdField.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void cancel(ActionEvent event) {
        // Close the window
        Stage stage = (Stage) ticketIdField.getScene().getWindow();
        stage.close();
    }
}