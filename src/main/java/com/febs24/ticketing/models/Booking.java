package com.febs24.ticketing.models;

import com.febs24.ticketing.dao.TicketDao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;

public class Booking {

    private int bookingId;
    private int ticketId;
    private int userId;
    private String guestEmail;
    private int quantity;
    private double totalPrice;
    private Ticket ticket;

    // Default constructor
    public Booking() {
    }

    // Parameterized constructor
    public Booking(int ticketId, int userId, String guestEmail,int quantity, double totalPrice, Connection connection) throws SQLException {
        this.ticketId = ticketId;
        this.userId = userId;
        this.guestEmail = guestEmail;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        TicketDao ticketDao = new TicketDao(connection);
        this.ticket = ticketDao.getTicket(ticketId);
    }

    // Parameterized constructor
    public Booking(int bookingId, int ticketId, int userId, String guestEmail, int quantity, double totalPrice, Connection connection) throws SQLException {
        this.bookingId = bookingId;
        this.ticketId = ticketId;
        this.userId = userId;
        this.guestEmail = guestEmail;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        TicketDao ticketDao = new TicketDao(connection);
        this.ticket = ticketDao.getTicket(ticketId);
    }

    // Getters and Setters
    public int getBookingId() {
        return bookingId;
    }

    public int getTicketId() {
        return ticketId;
    }

    public int getUserId() {
        return userId;
    }

    public String getGuestEmail() {
        return guestEmail;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) { // Add this method
        this.ticket = ticket;
    }

    // Save booking to database (requires java.sql.Connection object)
    public boolean saveBooking(Connection connection) throws SQLException {
        String sql = "INSERT INTO Bookings (ticket_id, user_id, guest_email, quantity, total_price) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, this.ticketId);
            stmt.setInt(2, this.userId);
            stmt.setString(3, this.guestEmail);
            stmt.setInt(4, this.quantity);
            stmt.setDouble(5, this.totalPrice);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Load a Booking object from a ResultSet (requires java.sql.Connection object)
    public static Booking fromResultSet(ResultSet rs, Connection connection) throws SQLException {
        Booking booking = new Booking();
        booking.bookingId = rs.getInt("booking_id");
        booking.ticketId = rs.getInt("ticket_id");
        booking.userId = rs.getInt("user_id");
        booking.guestEmail = rs.getString("guest_email");
        booking.quantity = rs.getInt("quantity");
        booking.totalPrice = rs.getDouble("total_price");
        return booking;
    }

    // toString method
    @Override
    public String toString() {
        if (ticket == null) {
            return "Booking details are incomplete. Ticket is missing.";
        }
        Game game = ticket.getGame();
        if (game == null) {
            return "Booking details are incomplete. Game details are missing.";
        }
        String lastWordHomeTeam = getLastWord(game.getHomeTeam());
        String lastWordAwayTeam = getLastWord(game.getAwayTeam());
        return lastWordHomeTeam + " vs " + lastWordAwayTeam + ", "+ quantity + " tickets at: $" + totalPrice + " total.";
    }

    private String getLastWord(String teamName) {
        if (teamName != null && !teamName.isEmpty()) {
            String[] words = teamName.split(" ");
            return words[words.length - 1];
        }
        return "Unknown";
    }
}