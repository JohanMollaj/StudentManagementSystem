import java.sql.*;
import java.util.ArrayList;

public class GradeDAO {

    public boolean insert(Grade g) {
        String sql = "INSERT INTO grades (student_id, course_id, grade, semester) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, g.getStudentId());
            ps.setString(2, g.getCourseId());
            ps.setDouble(3, g.getGrade());
            ps.setString(4, g.getSemester());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Gabim insert grade: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int gradeId) {
        String sql = "DELETE FROM grades WHERE grade_id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, gradeId);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Gabim delete grade: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<Grade> getByStudent(String studentId) {
        ArrayList<Grade> list = new ArrayList<>();
        String sql = "SELECT * FROM grades WHERE student_id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, studentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Grade(
                        rs.getInt("grade_id"),
                        rs.getString("student_id"),
                        rs.getString("course_id"),
                        rs.getDouble("grade"),
                        rs.getString("semester")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Gabim getByStudent: " + e.getMessage());
        }
        return list;
    }

    public double getAverageGrade(String studentId) {
        String sql = "SELECT AVG(grade) AS avg_grade FROM grades WHERE student_id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, studentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble("avg_grade");

        } catch (SQLException e) {
            System.out.println("Gabim getAverageGrade: " + e.getMessage());
        }
        return 0.0;
    }
}