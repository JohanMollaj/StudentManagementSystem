import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String URL =
            "jdbc:sqlserver://DESKTOP-SASDDMN\\SQLEXPRESS:1433;"
                    + "databaseName=StudentDB;"
                    + "encrypt=false;"
                    + "trustServerCertificate=true;"
                    + "integratedSecurity=true;";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("Gabim gjate mbylljes se lidhjes: " + e.getMessage());
            }
        }
    }

    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            JOptionPane.showMessageDialog(null, "✓ Lidhja me databaze u krye me sukses!");
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "✗ Lidhja deshtoi: " + e.getMessage(),
                    "Gabim Databaze", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}