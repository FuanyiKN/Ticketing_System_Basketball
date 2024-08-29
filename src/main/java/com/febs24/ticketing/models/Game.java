package com.febs24.ticketing.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class Game {
    private int gameId;
    private LocalDate date;
    private LocalTime time;
    private String homeTeam;
    private String awayTeam;

    // Default constructor
    public Game() {}

    // Parameterized constructor
    public Game(int gameId, LocalDate date, LocalTime time, String homeTeam, String awayTeam) {
        this.gameId = gameId;
        this.date = date;
        this.time = time;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
    }

    public Game(LocalDate date, LocalTime time, String homeTeam, String awayTeam) {
        this.date = date;
        this.time = time;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
    }

    // Getters
    public int getGameId() { return gameId; }
    public LocalDate getDate() { return date; }
    public LocalTime getTime() { return time; }
    public String getHomeTeam() { return homeTeam; }
    public String getAwayTeam() { return awayTeam; }

    // Setters
    public void setGameId(int gameId) { this.gameId = gameId; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setTime(LocalTime time) { this.time = time; }
    public void setHomeTeam(String homeTeam) { this.homeTeam = homeTeam; }
    public void setAwayTeam(String awayTeam) { this.awayTeam = awayTeam; }

    // Convert a ResultSet into a Game object
    public static Game fromResultSet(ResultSet rs) {
        try {
            return new Game(
                    rs.getInt("game_id"),
                    rs.getDate("date").toLocalDate(),
                    rs.getTime("time").toLocalTime(),
                    rs.getString("home_team"),
                    rs.getString("away_team")
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    // toString method
    @Override
    public String toString() {
        return homeTeam + " vs " + awayTeam + "\n\t\t" + date + " at " + time;
    }
}
