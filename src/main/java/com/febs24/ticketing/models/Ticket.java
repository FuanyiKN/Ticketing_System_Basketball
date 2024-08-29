package com.febs24.ticketing.models;

import com.febs24.ticketing.dao.GameDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;

public class Ticket {

    private int ticketId;
    private int gameId;
    private int seatNumber;
    private String section;
    private double price;
    private Game game;

    // Default constructor
    public Ticket() {
    }

    // Parameterized constructor
    public Ticket(int gameId, int seatNumber, String section, double price, Connection connection) throws SQLException {
        this.gameId = gameId;
        this.seatNumber = seatNumber;
        this.section = section;
        this.price = price;
        setGame(connection);
    }

    public Ticket(int ticketId, int gameId, int seatNumber, String section, double price, Connection connection) throws SQLException {
        this.ticketId = ticketId;
        this.gameId = gameId;
        this.seatNumber = seatNumber;
        this.section = section;
        this.price = price;
        setGame(connection);
    }

    // Getters and Setters
    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    /// Save ticket to database (requires java.sql.Connection object)
    public boolean saveTicket(Connection connection) throws SQLException {
        String sql = "INSERT INTO Tickets (game_id, seat_number, section, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, this.gameId);
            stmt.setInt(2, this.seatNumber);
            stmt.setString(3, this.section);
            stmt.setDouble(4, this.price);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Load a Ticket object from a ResultSet (requires java.sql.Connection object)
    public static Ticket fromResultSet(ResultSet rs, Connection connection) throws SQLException {
        int ticketId = rs.getInt("ticket_id");
        int gameId = rs.getInt("game_id");
        int seatNumber = rs.getInt("seat_number");
        String section = rs.getString("section");
        double price = rs.getDouble("price");

        return new Ticket(ticketId, gameId, seatNumber, section, price, connection);
    }

    public void createTicket(Connection connection) throws SQLException {
        GameDao gameDao = new GameDao(connection);
        Ticket ticket = new Ticket(gameId, seatNumber, section, price, connection);
        ticket.setGame(connection);
    }

    public void setGame(Connection connection) throws SQLException {
        GameDao gameDao = new GameDao(connection);
        this.game = gameDao.getGame(this.gameId);
    }

    public Game getGame() {
        return game;
    }

    @Override
    public String toString() {
        if (game == null) {
            return "Game information is not available";
        }
        String lastWordHomeTeam = getLastWordInTeamName(game.getHomeTeam());
        String lastWordAwayTeam = getLastWordInTeamName(game.getAwayTeam());
        return "\t" + lastWordHomeTeam + " vs " + lastWordAwayTeam + "\n" + section + ", " + "Seat Number: " + seatNumber + ", " + "$" + price;
    }

    private String getLastWordInTeamName(String teamName) {
        String[] words = teamName.split(" ");
        return words[words.length - 1];
    }
}