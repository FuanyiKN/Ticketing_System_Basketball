package com.febs24.ticketing.controllers;

import com.febs24.ticketing.DatabaseConnection;
import com.febs24.ticketing.dao.GameDao;
import com.febs24.ticketing.dao.TicketDao;
import com.febs24.ticketing.models.Game;
import com.febs24.ticketing.models.Ticket;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import java.util.ArrayList;

public class FebsController {

    @FXML
    private ComboBox<String> teamComboBox;

    @FXML
    private ListView<Game> gamesListView;

    @FXML
    private ListView<Ticket> ticketsListView;

    @FXML
    private Button signInButton;

    @FXML
    private Label febsLabel;

    private GameDao gameDao;
    private TicketDao ticketDao;

    public FebsController() {
        this.gameDao = new GameDao(DatabaseConnection.getConnection());
        this.ticketDao = new TicketDao(DatabaseConnection.getConnection());
    }

    public void copyGameToClipboard() {
        Game selectedGame = gamesListView.getSelectionModel().getSelectedItem();
        if (selectedGame != null) {
            String textToCopy = selectedGame.toString();
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString(textToCopy);
            clipboard.setContent(content);
        }
    }

    // This method will be called when the application starts
    @FXML
    public void initialize() {
        try {
            // Add an "All Teams" option to the teamComboBox
            teamComboBox.getItems().add("All Teams");

            // Populate the teamComboBox with teams from the database
            teamComboBox.getItems().addAll(gameDao.getAllTeams());

            // Fetch all games and tickets from the database
            List<Game> games = gameDao.getAllGames();
            List<Ticket> tickets = ticketDao.getAllTickets();

            // Display all games and tickets
            gamesListView.getItems().addAll(games);
            ticketsListView.getItems().addAll(tickets);

            // Create a context menu
            ContextMenu contextMenu = new ContextMenu();

            // Create a menu item
            MenuItem copyMenuItem = new MenuItem("Copy");
            copyMenuItem.setOnAction(event -> copyGameToClipboard());

            // Add the menu item to the context menu
            contextMenu.getItems().add(copyMenuItem);

            // Set the context menu on the list view
            gamesListView.setContextMenu(contextMenu);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // This method will be called when a team is selected from the teamComboBox
    @FXML
    public void teamSelected(ActionEvent event) {
        String selectedTeam = teamComboBox.getSelectionModel().getSelectedItem();
        try {
            if ("All Teams".equals(selectedTeam)) {
                // Display all games and tickets
                gamesListView.getItems().setAll(gameDao.getAllGames());
                ticketsListView.getItems().setAll(ticketDao.getAllTickets());
            } else {
                // Display game and ticket details for the selected team
                List<Game> games = gameDao.getGamesByTeam(selectedTeam);
                gamesListView.getItems().clear();
                gamesListView.getItems().addAll(games);

                List<Ticket> tickets = new ArrayList<>();
                for (Game game : games) {
                    tickets.addAll(ticketDao.getTicketsByGame(game.getGameId()));
                }
                ticketsListView.getItems().clear();
                ticketsListView.getItems().addAll(tickets);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // This method will be called when the "Sign In" button is clicked
    @FXML
    public void signIn(ActionEvent event) {
        try {
            // Load the login form
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/febs24/ticketing/sign-in.fxml"));
            Parent root = loader.load();

            // Show the login form in a new stage
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}