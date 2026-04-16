package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleDatabase {
    private static boolean available = false;

    public static boolean init() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Oracle JDBC driver not found. Add ojdbc jar to the classpath.");
            available = false;
            return false;
        }

        try (Connection conn = getConnection()) {
            available = conn != null && !conn.isClosed();
            if (available) {
                System.out.println("Connected to Oracle database successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to connect to Oracle database: " + e.getMessage());
            available = false;
        }
        return available;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
    }

    public static boolean isAvailable() {
        return available;
    }
}
