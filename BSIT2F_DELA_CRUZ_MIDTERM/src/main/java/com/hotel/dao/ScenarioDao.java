package com.hotel.dao;

import com.hotel.model.Reservation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** All SQL for hotel reservations lives here. */
public class ScenarioDao {
    private static final String INSERT_SQL =
            "INSERT INTO reservations (guest_name, room_type, nightly_rate, payment) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_SQL =
            "SELECT id, guest_name, room_type, nightly_rate, payment FROM reservations ORDER BY id";

    public ScenarioDao() {
        DatabaseConnection.initializeDatabase();
    }

    /** Inserts a new reservation using a PreparedStatement. */
    public void insert(Reservation r) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {
            ps.setString(1, r.getGuestName());
            ps.setString(2, r.getRoomType());
            ps.setDouble(3, r.getNightlyRate());
            ps.setString(4, r.getPayment());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Could not save reservation: " + e.getMessage(), e);
        }
    }

    /** Returns every reservation, ordered by ID. */
    public List<Reservation> findAll() {
        List<Reservation> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Reservation(
                        rs.getInt("id"),
                        rs.getString("guest_name"),
                        rs.getString("room_type"),
                        rs.getDouble("nightly_rate"),
                        rs.getString("payment")));
            }
        } catch (SQLException e) {
            throw new DaoException("Could not load reservations: " + e.getMessage(), e);
        }
        return list;
    }
}
