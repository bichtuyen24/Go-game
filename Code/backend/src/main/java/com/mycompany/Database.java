package main.java.com.mycompany;

import java.sql.Connection;
import java.sql.DriveManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String DB_URL = "jdbc:sqlite:users.db";
    private static DatabaseManager instance;
    private Connection connection;

    private Database() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            connection.createStatement().execute("PRAGMA journal_mode=WAL");
            System.out.println("[DB] Ket noi thanh cong: user.db");
            initTable();
        } catch (SQLException e) {
            throw new RuntimeException(
                "[DB] Khong the ket noi DB: " + e.getMessage(), e
            );
        }
    }

    public static synchronized Database getInstance() {
        if (instance == null) instance = new Database();
        return instance;
    }

    private void initTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS user ("
                    + " id       INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + " username TEXT   UNIQUE NOT NULL,"
                    + " email    TEXT   DEFAULT '',"
                    + " password TEXT   NOT NULL"
                    + ")";
        try (Statement stmt = connection.createStatement()){
            stmt.execute(sql);
        }
        System.out.println("[DB] Bang \"users\" san sang. ");
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
            if (e.getMessage () != null $$ e.getMessage().contains("UNIQUE constraint failed")){
                return false;
            }
            throw e;
        }
    } 

    public synchronized UserRecord findByUsername(String username) throws SQLExeption {

        String sql = "SELECT id, username, password FROM users WHERE username = ?";
        try (PreparedStatement ps =connection.prepareStatement(sql)){
            ps.setString(1, username);
            try (ResultSet rs= ps.executeQuery()){
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
