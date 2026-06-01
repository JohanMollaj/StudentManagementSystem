package model;

import java.time.LocalDate;
import java.time.Period;

/**
 * Klasa Student - modeli kryesor i të dhënave.
 * Çdo student në sistem ka këto atribute.
 * Kjo klasë nuk bën asgjë tjetër veç mban të dhënat dhe ofron
 * metoda të thjeshta për t'i marrë / ndryshuar ato.
 */
public class Student {

    // Atributet private - nuk i akseson kush direkt nga jashtë
    private String studentId;    // p.sh. "S001"
    private String firstName;    // emri
    private String lastName;     // mbiemri
    private String email;        // email-i
    private LocalDate birthDate; // data e lindjes (vit-muaj-dite)
    private String status;       // "Aktiv" ose "Joaktiv"
    private String gender;       // "Mashkull" ose "Femër"

    /**
     * Konstruktori - krijon një student të ri me të gjitha të dhënat.
     * Thirret kur shtojmë student të ri.
     */
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

    // ===================== GETTERS =====================
    // Metoda për të marrë (lexuar) vlerat e atributeve

    public String getStudentId() { return studentId; }
    public String getFirstName()  { return firstName; }
    public String getLastName()   { return lastName; }
    public String getEmail()      { return email; }
    public LocalDate getBirthDate(){ return birthDate; }
    public String getStatus()     { return status; }
    public String getGender()     { return gender; }

    // ===================== SETTERS =====================
    // Metoda për të ndryshuar vlerat e atributeve

    public void setStudentId(String studentId) { this.studentId = studentId; }
    public void setFirstName(String firstName)  { this.firstName = firstName; }
    public void setLastName(String lastName)    { this.lastName = lastName; }
    public void setEmail(String email)          { this.email = email; }
    public void setBirthDate(LocalDate birthDate){ this.birthDate = birthDate; }
    public void setStatus(String status)        { this.status = status; }
    public void setGender(String gender)        { this.gender = gender; }

    /**
     * Llogarit moshën e studentit nga data e lindjes deri sot.
     * Përdor klasën Period nga java.time për llogaritje të saktë.
     *
     * @return mosha si numër i plotë (vite)
     */
    public int calculateAge() {
        if (birthDate == null) return 0;
        // Period.between llogarit ndryshimin midis dy datave
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    /**
     * Kthen emrin e plotë të studentit (emri + mbiemri).
     * Praktik për shfaqje në tabelë ose mesazhe.
     *
     * @return emri i plotë si String
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * toString() - shfaqje e lexueshme e studentit.
     * Java e thirr automatikisht kur printojmë objektin.
     */
    @Override
    public String toString() {
        return "Student{ID='" + studentId + "', Emri='" + getFullName() +
                "', Email='" + email + "', Statusi='" + status + "'}";
    }
}