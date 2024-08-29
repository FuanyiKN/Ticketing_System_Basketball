package com.febs24.ticketing.controllers;

import com.febs24.ticketing.DatabaseConnection;
import com.febs24.ticketing.dao.GameDao;
import com.febs24.ticketing.models.Game;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class AddGameController {

    private GameDao gameDao;

    public AddGameController() {
        this.gameDao = new GameDao(DatabaseConnection.getConnection());
    }

    @FXML
    private TextField gameDateField;

    @FXML
    private TextField gameTimeField;

    @FXML
    private TextField homeTeamField;

    @FXML
    private TextField awayTeamField;

    @FXML
    public void submit(ActionEvent event) {
        LocalDate gameDate = LocalDate.parse(gameDateField.getText());
        LocalTime gameTime = LocalTime.parse(gameTimeField.getText());
        String homeTeam = homeTeamField.getText();
        String awayTeam = awayTeamField.getText();

        // Create new game
        Game newGame = new Game(gameDate, gameTime, homeTeam, awayTeam);

        // Save new game to database
        try {
            gameDao.createGame(newGame);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Close the window
        Stage stage = (Stage) gameDateField.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void cancel(ActionEvent event) {
        // Close the window
        Stage stage = (Stage) gameDateField.getScene().getWindow();
        stage.close();
    }
}
