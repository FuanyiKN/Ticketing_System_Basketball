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

public class UpdateGameController {

    @FXML
    private TextField gameIdField;

    @FXML
    private TextField gameDateField;

    @FXML
    private TextField gameTimeField;

    @FXML
    private TextField homeTeamField;

    @FXML
    private TextField awayTeamField;

    private GameDao gameDao;

    public UpdateGameController() {
        this.gameDao = new GameDao(DatabaseConnection.getConnection());
    }

    @FXML
    public void update(ActionEvent event) {
        int gameId = Integer.parseInt(gameIdField.getText());
        LocalDate gameDate = LocalDate.parse(gameDateField.getText());
        LocalTime gameTime = LocalTime.parse(gameTimeField.getText());
        String homeTeam = homeTeamField.getText();
        String awayTeam = awayTeamField.getText();

        // Create new game object with updated values
        Game updatedGame = new Game(gameId, gameDate, gameTime, homeTeam, awayTeam);

        // Update game in database
        try {
            gameDao.updateGame(updatedGame);

            // Retrieve the game from the database
            Game savedGame = gameDao.getGame(gameId);

            // Use savedGame for further operations
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
