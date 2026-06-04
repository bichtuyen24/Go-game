import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
public class Database {
    private static final String DB_URL = "jdbc:sqlite:game.db";
    
    public static boolean initDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            String sql = "CREATE TABLE IF NOT EXISTS players (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "username TEXT NOT NULL UNIQUE, " +
                        "password TEXT NOT NULL)";
            stmt.execute(sql);
            System.out.println("Database created!");
            return true;
        } catch (Exception e) {
            System.out.println("Error creating database: " + e.getMessage());
            return false;
        }
    }
}