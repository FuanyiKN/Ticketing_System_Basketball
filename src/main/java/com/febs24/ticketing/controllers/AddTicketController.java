package com.febs24.ticketing.controllers;

import com.febs24.ticketing.DatabaseConnection;
import com.febs24.ticketing.dao.TicketDao;
import com.febs24.ticketing.models.Ticket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class AddTicketController {

    @FXML
    private TextField gameIdField;

    @FXML
    private TextField seatNumberField;

    @FXML
    private TextField sectionField;

    @FXML
    private TextField priceField;

    private TicketDao ticketDao;

    public AddTicketController() {
        this.ticketDao = new TicketDao(DatabaseConnection.getConnection());
    }

    @FXML
    public void submit(ActionEvent event) {
        int gameId = Integer.parseInt(gameIdField.getText());
        int seatNumber = Integer.parseInt(seatNumberField.getText());
        String section = sectionField.getText();
        double price = Double.parseDouble(priceField.getText());

        // Create new ticket
        try {
            Ticket newTicket = new Ticket(gameId, seatNumber, section, price, DatabaseConnection.getConnection());

            // Save new ticket to database
            ticketDao.createTicket(newTicket);

            // Retrieve the ticket from the database
            Ticket savedTicket = ticketDao.getTicket(newTicket.getTicketId());

            // Use savedTicket for further operations
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Close the window
        Stage stage = (Stage) gameIdField.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void cancel(ActionEvent event) {
        // Close the window
        Stage stage = (Stage) gameIdField.getScene().getWindow();
        stage.close();
    }
}