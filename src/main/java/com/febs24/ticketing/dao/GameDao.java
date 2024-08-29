package com.febs24.ticketing.dao;

import com.febs24.ticketing.models.Game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GameDao {
    private Connection connection;

    public GameDao(Connection connection) {
        this.connection = connection;
    }

    // Method to create a new game
    public void createGame(Game game) throws SQLException {
        String sql = "INSERT INTO Games (date, time, home_team, away_team) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setDate(1, java.sql.Date.valueOf(game.getDate()));
        statement.setTime(2, java.sql.Time.valueOf(game.getTime()));
        statement.setString(3, game.getHomeTeam());
        statement.setString(4, game.getAwayTeam());
        statement.executeUpdate();
    }

    // Method to get a game by id
    public Game getGame(int gameId) throws SQLException {
        String sql = "SELECT * FROM Games WHERE game_id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, gameId);
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            return Game.fromResultSet(rs);
        } else {
            return null;
        }
    }

    // Method to get all games
    public List<Game> getAllGames() throws SQLException {
        String sql = "SELECT * FROM Games";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Game> games = new ArrayList<>();
        while (rs.next()) {
            games.add(Game.fromResultSet(rs));
        }
        return games;
    }

    // Method to get all games by team
    public List<Game> getGamesByTeam(String team) throws SQLException {
        String sql = "SELECT * FROM Games WHERE home_team = ? OR away_team = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, team);
        statement.setString(2, team);
        ResultSet rs = statement.executeQuery();
        List<Game> games = new ArrayList<>();
        while (rs.next()) {
            games.add(Game.fromResultSet(rs));
        }
        return games;
    }

    ////Method to retrieve all teams, both homeTeams and awayTeams
    public List<String> getAllTeams() throws SQLException {
        String sql = "SELECT home_team FROM Games UNION SELECT away_team FROM Games";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<String> teams = new ArrayList<>();
        while (rs.next()) {
            teams.add(rs.getString("home_team"));
        }
        return teams;
    }


    // Method to update an existing game
    public void updateGame(Game game) throws SQLException {
        String sql = "UPDATE Games SET date = ?, time = ?, home_team = ?, away_team = ? WHERE game_id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setDate(1, java.sql.Date.valueOf(game.getDate()));
        statement.setTime(2, java.sql.Time.valueOf(game.getTime()));
        statement.setString(3, game.getHomeTeam());
        statement.setString(4, game.getAwayTeam());
        statement.setInt(5, game.getGameId());
        statement.executeUpdate();
    }

    // Method to delete a game
    public void deleteGame(int gameId) throws SQLException {
        String sql = "DELETE FROM Games WHERE game_id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, gameId);
        statement.executeUpdate();
    }
}