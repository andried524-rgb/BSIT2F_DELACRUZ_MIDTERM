package com.hotel.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/** Opens SQLite connections and creates the table on startup. */
public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:hotel.db";

    private static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS reservations ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "guest_name TEXT NOT NULL, "
            + "room_type TEXT NOT NULL, "
            + "nightly_rate REAL NOT NULL, "
            + "payment TEXT NOT NULL)";

    private DatabaseConnection() { }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    /** Creates the reservations table if it does not exist yet. */
    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(CREATE_TABLE);
        } catch (SQLException e) {
            throw new DaoException("Could not initialize database: " + e.getMessage(), e);
        }
    }
}
