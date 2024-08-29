package com.febs24.ticketing.controllers;

import com.febs24.ticketing.DatabaseConnection;
import com.febs24.ticketing.dao.TicketDao;
import com.febs24.ticketing.models.Ticket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class UpdateTicketController {

    @FXML
    private TextField ticketIdField;

    @FXML
    private TextField gameIdField;

    @FXML
    private TextField seatNumberField;

    @FXML
    private TextField sectionField;

    @FXML
    private TextField priceField;

    private TicketDao ticketDao;

    public UpdateTicketController() {
        this.ticketDao = new TicketDao(DatabaseConnection.getConnection());
    }

    @FXML
    public void update(ActionEvent event) throws SQLException {
        int ticketId = Integer.parseInt(ticketIdField.getText());
        int gameId = Integer.parseInt(gameIdField.getText());
        int seatNumber = Integer.parseInt(seatNumberField.getText());
        String section = sectionField.getText();
        double price = Double.parseDouble(priceField.getText());

        // Create new ticket object with updated values
        Ticket updatedTicket = new Ticket(gameId, seatNumber, section, price, DatabaseConnection.getConnection());
        updatedTicket.setTicketId(ticketId);

        // Update ticket in database
        try {
            ticketDao.updateTicket(updatedTicket);

            // Retrieve the ticket from the database
            Ticket savedTicket = ticketDao.getTicket(ticketId);

            // Use savedTicket for further operations
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