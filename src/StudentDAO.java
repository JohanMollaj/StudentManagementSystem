import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class StudentDAO {

    public boolean insert(Student s) {
        String sql = "INSERT INTO students VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, s.getStudentId());
            ps.setString(2, s.getFirstName());
            ps.setString(3, s.getLastName());
            ps.setString(4, s.getEmail());
            ps.setDate(5, s.getBirthDate() != null ? Date.valueOf(s.getBirthDate()) : null);
            ps.setString(6, s.getGender());
            ps.setString(7, s.getStatus());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Gabim insert student: " + e.getMessage());
            return false;
        }
    }

    public boolean update(Student s) {
        String sql = "UPDATE students SET first_name=?, last_name=?, email=?, " +
                "birth_date=?, gender=?, status=? WHERE student_id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, s.getFirstName());
            ps.setString(2, s.getLastName());
            ps.setString(3, s.getEmail());
            ps.setDate(4, s.getBirthDate() != null ? Date.valueOf(s.getBirthDate()) : null);
            ps.setString(5, s.getGender());
            ps.setString(6, s.getStatus());
            ps.setString(7, s.getStudentId());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Gabim update student: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(String studentId) {
        String sql = "DELETE FROM students WHERE student_id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, studentId);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Gabim delete student: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<Student> getAll() {
        ArrayList<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Date bd = rs.getDate("birth_date");
                Student s = new Student(
                        rs.getString("student_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        bd != null ? bd.toLocalDate() : null,
                        rs.getString("status"),
                        rs.getString("gender")  // ← shto kete
                );
                s.setGender(rs.getString("gender"));
                list.add(s);
            }

        } catch (SQLException e) {
            System.out.println("Gabim getAll students: " + e.getMessage());
        }
        return list;
    }

    public Student findById(String studentId) {
        String sql = "SELECT * FROM students WHERE student_id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, studentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Date bd = rs.getDate("birth_date");
                Student s = new Student(
                        rs.getString("student_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        bd != null ? bd.toLocalDate() : null,
                        rs.getString("status"),
                        rs.getString("gender")  // ← shto kete
                );
                s.setGender(rs.getString("gender"));
                return s;
            }

        } catch (SQLException e) {
            System.out.println("Gabim findById: " + e.getMessage());
        }
        return null;
    }

    public ArrayList<Student> searchByName(String name) {
        ArrayList<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE first_name LIKE ? OR last_name LIKE ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + name + "%");
            ps.setString(2, "%" + name + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Date bd = rs.getDate("birth_date");
                Student s = new Student(
                        rs.getString("student_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        bd != null ? bd.toLocalDate() : null,
                        rs.getString("status"),
                        rs.getString("gender")  // ← shto kete
                );
                s.setGender(rs.getString("gender"));
                list.add(s);
            }

        } catch (SQLException e) {
            System.out.println("Gabim searchByName: " + e.getMessage());
        }
        return list;
    }
}