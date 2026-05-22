import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnection {
    public static void main(String[] args) {
        String serverName = "DESKTOP-SASDDMN";

        String url = "jdbc:sqlserver://DESKTOP-SASDDMN\\SQLEXPRESS:1433;"
                + "databaseName=StudentDB;"
                + "encrypt=false;"
                + "trustServerCertificate=true;"
                + "integratedSecurity=true;";

        System.out.println("Duke u lidhur me: " + url);

        try (Connection conn = DriverManager.getConnection(url)) {
            System.out.println("✓ Lidhja u krye me sukses!");
            System.out.println("Database: " + conn.getCatalog());
        } catch (SQLException e) {
            System.out.println("✗ Lidhja deshtoi!");
            System.out.println("Gabimi: " + e.getMessage());
            System.out.println("Error code: " + e.getErrorCode());
        }
    }
}