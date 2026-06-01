import java.sql.*;
import java.util.ArrayList;

public class CourseDAO {

    public boolean insert(Course c) {
        String sql = "INSERT INTO courses VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getCourseId());
            ps.setString(2, c.getCourseName());
            ps.setInt(3, c.getCredits());
            ps.setString(4, c.getLecturer());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Gabim insert course: " + e.getMessage());
            return false;
        }
    }

    public boolean update(Course c) {
        String sql = "UPDATE courses SET course_name=?, credits=?, lecturer=? WHERE course_id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getCourseName());
            ps.setInt(2, c.getCredits());
            ps.setString(3, c.getLecturer());
            ps.setString(4, c.getCourseId());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Gabim update course: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(String courseId) {
        String sql = "DELETE FROM courses WHERE course_id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, courseId);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Gabim delete course: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<Course> getAll() {
        ArrayList<Course> list = new ArrayList<>();
        String sql = "SELECT * FROM courses";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Course(
                        rs.getString("course_id"),
                        rs.getString("course_name"),
                        rs.getInt("credits"),
                        rs.getString("lecturer")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Gabim getAll courses: " + e.getMessage());
        }
        return list;
    }

    public Course findById(String courseId) {
        String sql = "SELECT * FROM courses WHERE course_id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, courseId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Course(
                        rs.getString("course_id"),
                        rs.getString("course_name"),
                        rs.getInt("credits"),
                        rs.getString("lecturer")
                );
            }

        } catch (SQLException e) {
            System.out.println("Gabim findById course: " + e.getMessage());
        }
        return null;
    }
}