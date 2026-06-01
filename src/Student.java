import java.time.LocalDate;
import java.time.Period;

public class Student {
    private String studentId;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
    private String gender;
    private String status;

    public Student(String studentId, String firstName, String lastName,
                   String email, LocalDate birthDate, String status, String gender) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthDate = birthDate;
        this.status = status;
        this.gender = gender;
    }

    public String getStudentId() { return studentId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getGender() { return gender; }
    public String getStatus() { return status; }

    public void setStudentId(String studentId) { this.studentId = studentId; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public void setGender(String gender) { this.gender = gender; }
    public void setStatus(String status) { this.status = status; }

    public int calculateAge() {
        if (birthDate == null) return 0;
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return studentId + " - " + getFullName();
    }
}