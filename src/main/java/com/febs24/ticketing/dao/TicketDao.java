package com.febs24.ticketing.dao;

import com.febs24.ticketing.models.Ticket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TicketDao {
    private Connection connection;

    public TicketDao(Connection connection) {
        this.connection = connection;
    }

    // Method to create a new ticket
    public void createTicket(Ticket ticket) throws SQLException {
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket cannot be null");
        }
        String sql = "INSERT INTO Tickets (game_id, seat_number, section, price) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, ticket.getGameId());
        statement.setInt(2, ticket.getSeatNumber());
        statement.setString(3, ticket.getSection());
        statement.setDouble(4, ticket.getPrice());
        statement.executeUpdate();
    }

    // Method to get a ticket by id
    public Ticket getTicket(int ticketId) throws SQLException {
        String sql = "SELECT * FROM Tickets WHERE ticket_id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, ticketId);
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            Ticket ticket = Ticket.fromResultSet(rs, connection);
            if (ticket == null) {
                throw new SQLException("Failed to create a ticket from the result set");
            }
            return ticket;
        } else {
            throw new SQLException("No ticket found with the provided ID");
        }
    }

    // Method to get all tickets
    public List<Ticket> getAllTickets() throws SQLException {
        String sql = "SELECT * FROM Tickets";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet rs = statement.executeQuery();
        List<Ticket> tickets = new ArrayList<>();
        while (rs.next()) {
            tickets.add(Ticket.fromResultSet(rs, connection));
        }
        return tickets;
    }

    // Method to get all tickets by game
    public List<Ticket> getTicketsByGame(int gameId) throws SQLException {
        String sql = "SELECT * FROM Tickets WHERE game_id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, gameId);
        ResultSet rs = statement.executeQuery();
        List<Ticket> tickets = new ArrayList<>();
        while (rs.next()) {
            tickets.add(Ticket.fromResultSet(rs, connection));
        }
        return tickets;
    }

    // Method to update an existing ticket
    public void updateTicket(Ticket ticket) throws SQLException {
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket cannot be null");
        }
        String sql = "UPDATE Tickets SET game_id = ?, seat_number = ?, section = ?, price = ? WHERE ticket_id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, ticket.getGameId());
        statement.setInt(2, ticket.getSeatNumber());
        statement.setString(3, ticket.getSection());
        statement.setDouble(4, ticket.getPrice());
        statement.setInt(5, ticket.getTicketId());
        statement.executeUpdate();
    }

    // Method to delete a ticket
    public void deleteTicket(int ticketId) throws SQLException {
        String sql = "DELETE FROM Tickets WHERE ticket_id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, ticketId);
        statement.executeUpdate();
    }
}