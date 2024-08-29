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
import javafx.scene.Node;
import javafx.scene.control.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.io.IOException;
import java.sql.Connection;

public class AdminProfileController {

    @FXML
    private ComboBox<String> teamComboBox;

    @FXML
    private ListView<Game> gamesListView;

    @FXML
    private ListView<Ticket> ticketsListView;

    @FXML
    private ListView<Booking> bookingsListView;

    @FXML
    private ListView<User> usersListView;

    @FXML
    private Button addGameButton, updateGameButton, deleteGameButton;

    @FXML
    private Button addTicketButton, updateTicketButton, deleteTicketButton;

    @FXML
    private Button addBookingButton, updateBookingButton, deleteBookingButton;

    @FXML
    private Button addUserButton, updateUserButton, deleteUserButton, makeAdminButton;

    private GameDao gameDao;
    private BookingDao bookingDao;
    private UserDao userDao;
    private TicketDao ticketDao;

    private Connection connection;

    public AdminProfileController() {
        this.connection = DatabaseConnection.getConnection();
        this.gameDao = new GameDao(connection);
        this.bookingDao = new BookingDao(connection);
        this.userDao = new UserDao(connection);
        this.ticketDao = new TicketDao(connection);
    }

    public void setGameDao(GameDao gameDao) {
        this.gameDao = gameDao;
    }

    public void setBookingDao(BookingDao bookingDao) {
        this.bookingDao = bookingDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setTicketDao(TicketDao ticketDao) {
        this.ticketDao = ticketDao;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public AdminProfileController(GameDao gameDao, BookingDao bookingDao, UserDao userDao, TicketDao ticketDao, Connection connection) {
        this.gameDao = gameDao;
        this.bookingDao = bookingDao;
        this.userDao = userDao;
        this.ticketDao = ticketDao;
        this.connection = connection;
    }

    @FXML
    public void initialize() throws SQLException {
        // Add an "All Teams" option to the teamComboBox
        teamComboBox.getItems().add("All Teams");

        // Populate the teamComboBox with teams from the Game table
        List<String> teams = gameDao.getAllTeams();
        teamComboBox.getItems().addAll(teams);

        // Fetch and display all games, tickets, bookings, and users
        displayAllGames();
        displayAllTickets();
        displayAllBookings();
        displayAllUsers();
        //Data Refresh
        refreshData();
    }

    private void displayAllGames() throws SQLException {
        // Fetch all games from the database
        List<Game> games = gameDao.getAllGames();

        // Display the games in the ListView
        gamesListView.getItems().setAll(games);
    }

    private void displayAllTickets() throws SQLException {
        // Fetch all tickets from the database
        List<Ticket> tickets = ticketDao.getAllTickets();

        // Display the tickets in the ListView
        ticketsListView.getItems().setAll(tickets);
    }

    private void displayAllBookings() throws SQLException {
        // Fetch all bookings from the database
        List<Booking> bookings = bookingDao.getAllBookings();

        // Display the bookings in the ListView
        bookingsListView.getItems().setAll(bookings);
    }

    private void displayAllUsers() throws SQLException {
        // Fetch all users from the database
        List<User> users = userDao.getAllUsers();

        // Display the users in the ListView
        usersListView.getItems().setAll(users);
    }

    @FXML
    void teamSelected(ActionEvent event) throws SQLException {
        String selectedTeam = teamComboBox.getSelectionModel().getSelectedItem();
        if ("All Teams".equals(selectedTeam)) {
            // Display all games and tickets
            displayAllGames();
            displayAllTickets();
        } else {
            // Display game and ticket details for the selected team
            List<Game> games = gameDao.getGamesByTeam(selectedTeam);
            gamesListView.getItems().setAll(games);
            List<Ticket> tickets = new ArrayList<>();
            for (Game game : games) {
                tickets.addAll(ticketDao.getTicketsByGame(game.getGameId()));
            }
            ticketsListView.getItems().setAll(tickets);
        }
    }

    private void refreshData() {
        try {
            // Fetch the latest data from the database
            List<Game> games = gameDao.getAllGames();
            List<Ticket> tickets = ticketDao.getAllTickets();
            List<User> users = userDao.getAllUsers();

            // Update the ListViews
            gamesListView.getItems().setAll(games);
            ticketsListView.getItems().setAll(tickets);
            usersListView.getItems().setAll(users);
        } catch (SQLException e) {
            // Handle the exception
            e.printStackTrace();
        }
    }

    @FXML
    void addGame(ActionEvent event) {
        // Open the AddGame.fxml window
        // This will require a new method to handle opening new windows
        openWindow("/com/febs24/ticketing/AddGame.fxml");
        //Data Refresh
        refreshData();
    }

    @FXML
    void updateGame(ActionEvent event) {
        // Open the UpdateGame.fxml window
        openWindow("/com/febs24/ticketing/UpdateGame.fxml");
        //Data Refresh
        refreshData();
    }

    @FXML
    void deleteGame(ActionEvent event) throws SQLException {
        // Delete the selected game
        Game selectedGame = gamesListView.getSelectionModel().getSelectedItem();
        gameDao.deleteGame(selectedGame.getGameId());
        //Data Refresh
        refreshData();
    }

    @FXML
    void addTicket(ActionEvent event) {
        // Open the AddTicket.fxml window
        openWindow("/com/febs24/ticketing/AddTicket.fxml");
        //Data Refresh
        refreshData();
    }

    @FXML
    void updateTicket(ActionEvent event) {
        // Open the UpdateTicket.fxml window
        openWindow("/com/febs24/ticketing/UpdateTicket.fxml");
        //Data Refresh
        refreshData();
    }

    @FXML
    void deleteTicket(ActionEvent event) throws SQLException {
        // Delete the selected Ticket
        Ticket selectedTicket = ticketsListView.getSelectionModel().getSelectedItem();
        ticketDao.deleteTicket(selectedTicket.getTicketId());
        //Data Refresh
        refreshData();
    }

    @FXML
    void addBooking(ActionEvent event) {
        // Open the AddBooking.fxml window
        openWindow("/com/febs24/ticketing/AddBooking.fxml");
        //Data Refresh
        refreshData();
    }

    @FXML
    void updateBooking(ActionEvent event) {
        // Open the UpdateBooking.fxml window
        openWindow("/com/febs24/ticketing/UpdateBooking.fxml");
        //Data Refresh
        refreshData();
    }

    @FXML
    void deleteBooking(ActionEvent event) throws SQLException {
        // Delete the selected Booking
        Booking selectedBooking = bookingsListView.getSelectionModel().getSelectedItem();
        bookingDao.deleteBooking(selectedBooking.getBookingId());
        //Data Refresh
        refreshData();
    }

    @FXML
    void addUser(ActionEvent event) {
        // Open the AddUser.fxml window
        openWindow("/com/febs24/ticketing/AddUser.fxml");
        //Data Refresh
        refreshData();
    }

    @FXML
    void updateUser(ActionEvent event) {
        // Open the UpdateUser.fxml window
        openWindow("/com/febs24/ticketing/UpdateUser.fxml");
        //Data Refresh
        refreshData();
    }

    @FXML
    void deleteUser(ActionEvent event) throws SQLException {
        User selectedUser = usersListView.getSelectionModel().getSelectedItem();
        userDao.deleteUser(selectedUser.getUserId(), connection);
        //Data Refresh
        refreshData();
    }

    @FXML
    void makeAdmin(ActionEvent event) throws SQLException {
        User selectedUser = usersListView.getSelectionModel().getSelectedItem();
        userDao.makeAdmin(selectedUser.getUserId(), connection);
    }

    @FXML
    void logout(ActionEvent event) {
        try {
            // Load the sign-in page
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/febs24/ticketing/febs.fxml"));
            Parent root = fxmlLoader.load();

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the scene of the current stage to the sign-in page
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}