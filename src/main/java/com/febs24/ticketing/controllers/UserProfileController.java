package com.febs24.ticketing.controllers;

import com.febs24.ticketing.DatabaseConnection;
import com.febs24.ticketing.dao.BookingDao;
import com.febs24.ticketing.dao.GameDao;
import com.febs24.ticketing.dao.TicketDao;
import com.febs24.ticketing.dao.UserDao;
import com.febs24.ticketing.models.Booking;
import com.febs24.ticketing.models.Game;
import com.febs24.ticketing.models.Ticket;
import com.febs24.ticketing.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.Button;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.control.Label;

public class UserProfileController {

    @FXML
    private ComboBox<String> teamComboBox;

    @FXML
    private ListView<Game> gamesListView;

    @FXML
    private ListView<Ticket> ticketsListView;

    @FXML
    private ListView<Booking> bookingsListView;

    @FXML
    private Spinner<Integer> quantitySpinner;

    @FXML
    private Button bookTicketButton;

    @FXML
    private Button updateBookingButton;

    @FXML
    private Button removeBookingButton;

    @FXML
    private Button updateButton;

    @FXML
    private Label userInfoLabel;

    private UserDao userDao;
    private GameDao gameDao;
    private TicketDao ticketDao;
    private BookingDao bookingDao;
    private User user;

    // Add a no-argument constructor
    public UserProfileController() {
        Connection connection = DatabaseConnection.getConnection();
        this.userDao = new UserDao(connection);
        this.gameDao = new GameDao(connection);
        this.ticketDao = new TicketDao(connection);
        this.bookingDao = new BookingDao(connection);
    }

    public UserProfileController(User user) {
        this();
        this.user = user;
    }

    public void setUser(User user) {
        this.user = user;
        updateButtonText();
    }

    private void updateButtonText() {
        if (user != null) {
            updateButton.setText(user.getUsername() + " ID: " + user.getUserId());
        }
    }

    @FXML
    public void initialize() throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        this.userDao = new UserDao(connection);
        this.gameDao = new GameDao(connection);
        this.ticketDao = new TicketDao(connection);
        this.bookingDao = new BookingDao(connection);

        // Populate the teamComboBox with teams from the database
        List<String> teams = gameDao.getAllTeams();
        teams.add(0, "All Teams"); // Add "All Teams" option
        teamComboBox.getItems().addAll(teams);

        // Fetch all bookings for the user from the database
        List<Booking> bookings = null;
        if (this.user != null) {
            bookings = bookingDao.getBookingsByUserId(user.getUserId());
            // Display all bookings
            bookingsListView.getItems().addAll(bookings);
        } else {
            System.out.println("User object is null.");
        }

        // Fetch and display all games and tickets
        List<Game> games = gameDao.getAllGames();
        gamesListView.getItems().addAll(games);
        List<Ticket> tickets = ticketDao.getAllTickets();
        ticketsListView.getItems().addAll(tickets);

        // Add listeners to enable/disable buttons
        ticketsListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            bookTicketButton.setDisable(newSelection == null);
        });
        bookingsListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            updateBookingButton.setDisable(newSelection == null);
            removeBookingButton.setDisable(newSelection == null);
        });

        // Check if user is not null before setting the button text
        if (user != null) {
            updateButton.setText(user.getUsername() + " ID: " + user.getUserId());
        }
    }

    @FXML
    public void teamSelected(ActionEvent event) throws SQLException {
        String selectedTeam = teamComboBox.getSelectionModel().getSelectedItem();

        // Fetch and display games for the selected team
        List<Game> games;
        if (selectedTeam.equals("All Teams")) {
            games = gameDao.getAllGames();
        } else {
            games = gameDao.getGamesByTeam(selectedTeam);
        }
        gamesListView.getItems().clear();
        gamesListView.getItems().addAll(games);

        // Fetch and display tickets for the games of the selected team
        List<Ticket> tickets = new ArrayList<>();
        for (Game game : games) {
            tickets.addAll(ticketDao.getTicketsByGame(game.getGameId()));
        }
        ticketsListView.getItems().clear();
        ticketsListView.getItems().addAll(tickets);
    }

    @FXML
    void openUpdateUser(ActionEvent event) {
        // Open the UpdateUser.fxml window
        openWindow("/com/febs24/ticketing/UpdateUser.fxml");
    }

    private void openWindow(String fxmlPath) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void bookTicket(ActionEvent event) throws SQLException {
        // Get selected ticket and quantity
        Ticket selectedTicket = ticketsListView.getSelectionModel().getSelectedItem();
        int quantity = quantitySpinner.getValue();

        // Calculate total price
        double totalPrice = selectedTicket.getPrice() * quantity;

        // Create and save new booking
        Booking newBooking = new Booking(selectedTicket.getTicketId(), user.getUserId(), user.getEmail(), quantity, totalPrice, DatabaseConnection.getConnection());
        newBooking.setTicket(selectedTicket); // Ensure a valid Ticket object is set
        bookingDao.createBooking(newBooking);

        // Update bookings list view
        bookingsListView.getItems().add(newBooking);
    }

    @FXML
    public void updateBooking(ActionEvent event) throws SQLException {
        // Get selected booking and new quantity
        Booking selectedBooking = bookingsListView.getSelectionModel().getSelectedItem();
        Booking newBooking = getNewBooking(selectedBooking);
        bookingDao.createBooking(newBooking);

        // Delete the selected booking
        bookingDao.deleteBooking(selectedBooking.getBookingId());

        // Clear and reload bookings list view
        bookingsListView.getItems().clear();
        List<Booking> bookings = bookingDao.getBookingsByUserId(user.getUserId());
        bookingsListView.getItems().addAll(bookings);
    }

    private Booking getNewBooking(Booking selectedBooking) throws SQLException {
        int newQuantity = quantitySpinner.getValue();

        // Calculate new total price
        double newTotalPrice = selectedBooking.getTotalPrice() / selectedBooking.getQuantity() * newQuantity;

        // Save the selected ticket's ID
        int selectedTicketId = selectedBooking.getTicketId();

        // Create a new booking with the saved ticket ID and the details from the spinner and total price
        return new Booking(selectedTicketId, user.getUserId(), user.getEmail(), newQuantity, newTotalPrice, DatabaseConnection.getConnection());
    }

    @FXML
    public void removeBooking(ActionEvent event) throws SQLException {
        // Get selected booking
        Booking selectedBooking = bookingsListView.getSelectionModel().getSelectedItem();

        // Delete booking
        bookingDao.deleteBooking(selectedBooking.getBookingId());

        // Update bookings list view
        bookingsListView.getItems().remove(selectedBooking);
    }

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        // Clear user data
        this.user = null;

        // Close the UserProfile window
        stage.close();
    }
}