package com.febs24.ticketing.controllers;

import com.febs24.ticketing.DatabaseConnection;
import com.febs24.ticketing.dao.BookingDao;
import com.febs24.ticketing.models.Booking;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class UpdateBookingController {

    @FXML
    private TextField bookingIdField;

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

    private final BookingDao bookingDao;

    public UpdateBookingController() {
        this.bookingDao = new BookingDao(DatabaseConnection.getConnection());
    }

    @FXML
    public void update(ActionEvent event) throws SQLException {
        int bookingId = Integer.parseInt(bookingIdField.getText());
        Booking updatedBooking = getBooking(bookingId);

        // Update booking in database
        try {
            bookingDao.updateBooking(updatedBooking);

            // Retrieve the booking from the database
            Booking savedBooking = bookingDao.getBooking(bookingId);

            // Use savedBooking for further operations
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Close the window
        Stage stage = (Stage) bookingIdField.getScene().getWindow();
        stage.close();
    }

    private Booking getBooking(int bookingId) throws SQLException {
        int ticketId = Integer.parseInt(ticketIdField.getText());
        int userId = Integer.parseInt(userIdField.getText());
        String guestEmail = guestEmailField.getText();
        int quantity = Integer.parseInt(quantityField.getText());
        double totalPrice = Double.parseDouble(totalPriceField.getText());

        // Create new booking object with updated values
        return new Booking(bookingId, ticketId, userId, guestEmail, quantity, totalPrice, DatabaseConnection.getConnection());
    }

    @FXML
    public void cancel(ActionEvent event) {
        // Close the window
        Stage stage = (Stage) bookingIdField.getScene().getWindow();
        stage.close();
    }
}