import java.time.LocalDate;
import java.time.Period;

public class Course {
    private String courseId;
    private String courseName;
    private int credits;
    private String lecturer;

    public Course(String courseId, String courseName, int credits, String lecturer) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.credits = credits;
        this.lecturer = lecturer;
    }

    public String getCourseId() { return courseId; }
    public String getCourseName() { return courseName; }
    public int getCredits() { return credits; }
    public String getLecturer() { return lecturer; }

    public void setCourseId(String courseId) { this.courseId = courseId; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public void setCredits(int credits) { this.credits = credits; }
    public void setLecturer(String lecturer) { this.lecturer = lecturer; }

    @Override
    public String toString() {
        return courseId + " - " + courseName;
    }

    public String getCourseInfo() {
        return "Kursi: " + courseName + " | Kredite: " + credits + " | Ligjëruesi: " + lecturer;
    }
}