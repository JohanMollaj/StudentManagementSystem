public class Grade {
    private int gradeId;
    private String studentId;
    private String courseId;
    private double grade;
    private String semester;

    public Grade(int gradeId, String studentId, String courseId, double grade, String semester) {
        this.gradeId = gradeId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.grade = grade;
        this.semester = semester;
    }

    public int getGradeId() { return gradeId; }
    public String getStudentId() { return studentId; }
    public String getCourseId() { return courseId; }
    public double getGrade() { return grade; }
    public String getSemester() { return semester; }

    public void setGradeId(int gradeId) { this.gradeId = gradeId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
    public void setGrade(double grade) { this.grade = grade; }
    public void setSemester(String semester) { this.semester = semester; }

    public String getLetterGrade() {
        if (grade >= 9) return "A";
        else if (grade >= 7) return "B";
        else if (grade >= 5) return "C";
        else if (grade >= 4) return "D";
        else return "F";
    }

    public boolean isPassing() {
        return grade >= 4;
    }
}