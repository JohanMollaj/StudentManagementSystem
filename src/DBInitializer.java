import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBInitializer {
    public static void initialize() {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {

            // krijon tabelen students nese nuk ekziston
            stmt.execute(
                    "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='students' AND xtype='U') " +
                            "CREATE TABLE students (" +
                            "    student_id  VARCHAR(10) PRIMARY KEY," +
                            "    first_name  VARCHAR(50) NOT NULL," +
                            "    last_name   VARCHAR(50) NOT NULL," +
                            "    email       VARCHAR(100) UNIQUE NOT NULL," +
                            "    birth_date  DATE," +
                            "    gender      VARCHAR(10)," +
                            "    status      VARCHAR(20) DEFAULT 'Aktiv'" +
                            ")"
            );

            // krijon tabelen courses nese nuk ekziston
            stmt.execute(
                    "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='courses' AND xtype='U') " +
                            "CREATE TABLE courses (" +
                            "    course_id   VARCHAR(10) PRIMARY KEY," +
                            "    course_name VARCHAR(100) NOT NULL," +
                            "    credits     INT NOT NULL," +
                            "    lecturer    VARCHAR(100)" +
                            ")"
            );

            // krijon tabelen grades nese nuk ekziston
            stmt.execute(
                    "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='grades' AND xtype='U') " +
                            "CREATE TABLE grades (" +
                            "    grade_id    INT IDENTITY PRIMARY KEY," +
                            "    student_id  VARCHAR(10) NOT NULL," +
                            "    course_id   VARCHAR(10) NOT NULL," +
                            "    grade       FLOAT NOT NULL," +
                            "    semester    VARCHAR(20)," +
                            "    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE," +
                            "    FOREIGN KEY (course_id)  REFERENCES courses(course_id)   ON DELETE CASCADE" +
                            ")"
            );

            System.out.println("✓ Tabelat u inicializuan me sukses!");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Gabim gjate inicializimit te tabelave: " + e.getMessage(),
                    "Gabim", JOptionPane.ERROR_MESSAGE);
        }
    }
}