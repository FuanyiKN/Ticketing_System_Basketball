package com.febs24.ticketing.dao;

import com.febs24.ticketing.models.Booking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookingDao {
    private final Connection connection;

    public BookingDao(Connection connection) {
        this.connection = connection;
    }

    // Method to create a new booking
    public void createBooking(Booking booking) throws SQLException {
        String sql = "INSERT INTO Bookings (ticket_id, user_id, guest_email, quantity, total_price) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, booking.getTicketId());
        statement.setInt(2, booking.getUserId());
        statement.setString(3, booking.getGuestEmail());
        statement.setInt(4, booking.getQuantity());
        statement.setDouble(5, booking.getTotalPrice());
        statement.executeUpdate();
    }

    // Method to get a booking by id
    public Booking getBooking(int bookingId) throws SQLException {
        String sql = "SELECT * FROM Bookings WHERE booking_id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, bookingId);
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            return Booking.fromResultSet(rs, connection);
        } else {
            return null;
        }
    }

    // Method to get all bookings by user id
    public List<Booking> getBookingsByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM Bookings WHERE user_id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, userId);
        ResultSet rs = statement.executeQuery();
        List<Booking> bookings = new ArrayList<>();
        while (rs.next()) {
            bookings.add(Booking.fromResultSet(rs, connection));
        }
        return bookings;
    }

    // Method to get all bookings
    public List<Booking> getAllBookings() throws SQLException {
        String sql = "SELECT * FROM Bookings";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet rs = statement.executeQuery();
        List<Booking> bookings = new ArrayList<>();
        while (rs.next()) {
            bookings.add(Booking.fromResultSet(rs, connection));
        }
        return bookings;
    }


    // Method to update an existing booking
    public void updateBooking(Booking booking) throws SQLException {
        String sql = "UPDATE Bookings SET ticket_id = ?, user_id = ?, guest_email = ?, quantity = ?, total_price = ? WHERE booking_id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, booking.getTicketId());
        statement.setInt(2, booking.getUserId());
        statement.setString(3, booking.getGuestEmail());
        statement.setInt(4, booking.getQuantity());
        statement.setDouble(5, booking.getTotalPrice());
        statement.setInt(6, booking.getBookingId());
        statement.executeUpdate();
    }

    // Method to delete a booking
    public void deleteBooking(int bookingId) throws SQLException {
        String sql = "DELETE FROM Bookings WHERE booking_id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, bookingId);
        statement.executeUpdate();
    }
}