package com.mycompany;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:users.db";
    private static DatabaseManager instance;
    private Connection connection;

    private final RoomManager roomManager = new RoomManager();

    private DatabaseManager() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("[DB] Ket noi thanh cong: users.db");

            initTable();

            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA journal_mode=WAL");
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "[DB] Khong the ket noi DB: " + e.getMessage(), e
            );
        }
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) instance = new DatabaseManager();
        return instance;
    }

    public Connection getConnection() {
        return this.connection;
    }

    private void initTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS users ("
                + " id       INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " username TEXT   UNIQUE NOT NULL,"
                + " email    TEXT   DEFAULT '',"
                + " password TEXT   NOT NULL"
                + ")";
        try (Statement stmt = connection.createStatement()){
            stmt.execute(sql);
        }
        System.out.println("[DB] Bang \"users\" san sang.");
    }

    public synchronized boolean insertUser(String username, String email, String hashedPwd) throws SQLException {
        String sql = "INSERT INTO users (username, email, password) VALUES (?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, email != null ? email : "");
            ps.setString(3, hashedPwd);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().contains("UNIQUE constraint failed")){
                return false;
            }
            throw e;
        }
    }

    public synchronized UserRecord findByUsername(String username) throws SQLException {
        String sql = "SELECT id, username, password FROM users WHERE username = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return new UserRecord(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password")
                    );
                }
                return null;
            }
        }
    }

    public synchronized boolean checkLogin(String username, String password) {
        String sql = "SELECT 1 FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("[DB Lỗi] Không thể xác thực đăng nhập: " + e.getMessage());
            return false;
        }
    }

    public synchronized boolean registerAccount(String username, String password) {
        try {
            return insertUser(username, "", password);
        } catch (SQLException e) {
            System.err.println("[DB Lỗi] Đăng ký thất bại: " + e.getMessage());
            return false;
        }
    }

    public RoomManager getRoomManager() {
        return this.roomManager;
    }

    public void close() {
        try {
            if(connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("[DB] Da dong ket noi.");
            }
        } catch (SQLException ignored) {}
    }

    public record UserRecord(int id, String username, String password) {}
}