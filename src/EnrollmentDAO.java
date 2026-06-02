import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class EnrollmentDAO {

    public boolean enroll(String studentId, String courseId) {
        String sql = "INSERT INTO enrollments (student_id, course_id) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ps.setString(2, courseId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            // UNIQUE constraint → studenti eshte regjistruar tashme
            if (e.getErrorCode() == 2627) {
                JOptionPane.showMessageDialog(null,
                        "Studenti eshte regjistruar tashme ne kete kurs!",
                        "Gabim", JOptionPane.ERROR_MESSAGE);
            } else {
                System.out.println("Gabim enroll: " + e.getMessage());
            }
            return false;
        }
    }

    public boolean unenroll(String studentId, String courseId) {
        String sql = "DELETE FROM enrollments WHERE student_id=? AND course_id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ps.setString(2, courseId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Gabim unenroll: " + e.getMessage());
            return false;
        }
    }

    // merr te gjithe kurset qe nje student eshte regjistruar
    public ArrayList<String> getCoursesByStudent(String studentId) {
        ArrayList<String> courseIds = new ArrayList<>();
        String sql = "SELECT course_id FROM enrollments WHERE student_id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) courseIds.add(rs.getString("course_id"));
        } catch (SQLException e) {
            System.out.println("Gabim getCoursesByStudent: " + e.getMessage());
        }
        return courseIds;
    }

    // merr te gjithe studentet e regjistruar ne nje kurs
    public ArrayList<String> getStudentsByCourse(String courseId) {
        ArrayList<String> studentIds = new ArrayList<>();
        String sql = "SELECT student_id FROM enrollments WHERE course_id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, courseId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) studentIds.add(rs.getString("student_id"));
        } catch (SQLException e) {
            System.out.println("Gabim getStudentsByCourse: " + e.getMessage());
        }
        return studentIds;
    }

    public boolean isEnrolled(String studentId, String courseId) {
        String sql = "SELECT COUNT(*) FROM enrollments WHERE student_id=? AND course_id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ps.setString(2, courseId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.out.println("Gabim isEnrolled: " + e.getMessage());
        }
        return false;
    }
}