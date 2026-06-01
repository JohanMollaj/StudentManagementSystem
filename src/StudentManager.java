import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class StudentManager {
    private ArrayList<Student> studentList = new ArrayList<>();
    private HashMap<String, Student> studentMap = new HashMap<>();
    private LinkedList<String> actionHistory = new LinkedList<>();

    private StudentDAO studentDAO = new StudentDAO();

    // ngarko te dhenat nga databaza kur starton aplikacioni
    public void loadAllFromDB() {
        studentList = studentDAO.getAll();
        studentMap.clear();
        for (Student s : studentList) {
            studentMap.put(s.getStudentId(), s);
        }
        logAction("U ngarkuan " + studentList.size() + " studente nga databaza");
    }

    public boolean addStudent(Student s) {
        if (studentMap.containsKey(s.getStudentId())) {
            return false; // ID ekziston
        }
        boolean success = studentDAO.insert(s);
        if (success) {
            studentList.add(s);
            studentMap.put(s.getStudentId(), s);
            logAction("U shtua studenti: " + s.getFullName());
        }
        return success;
    }

    public boolean updateStudent(Student s) {
        boolean success = studentDAO.update(s);
        if (success) {
            studentMap.put(s.getStudentId(), s);
            for (int i = 0; i < studentList.size(); i++) {
                if (studentList.get(i).getStudentId().equals(s.getStudentId())) {
                    studentList.set(i, s);
                    break;
                }
            }
            logAction("U modifikua studenti: " + s.getFullName());
        }
        return success;
    }

    public boolean deleteStudent(String studentId) {
        boolean success = studentDAO.delete(studentId);
        if (success) {
            Student removed = studentMap.remove(studentId);
            studentList.removeIf(s -> s.getStudentId().equals(studentId));
            if (removed != null) logAction("U fshi studenti: " + removed.getFullName());
        }
        return success;
    }

    public ArrayList<Student> getAllStudents() {
        return studentList;
    }

    public Student findById(String studentId) {
        return studentMap.get(studentId); // kërkim i shpejtë me HashMap
    }

    public ArrayList<Student> searchByName(String name) {
        ArrayList<Student> results = new ArrayList<>();
        for (Student s : studentList) {
            if (s.getFullName().toLowerCase().contains(name.toLowerCase())) {
                results.add(s);
            }
        }
        return results;
    }

    public ArrayList<Student> filterByStatus(String status) {
        ArrayList<Student> results = new ArrayList<>();
        for (Student s : studentList) {
            if (s.getStatus().equalsIgnoreCase(status)) {
                results.add(s);
            }
        }
        return results;
    }

    private void logAction(String action) {
        actionHistory.addFirst(action); // shto ne fillim te LinkedList
        if (actionHistory.size() > 20) {
            actionHistory.removeLast(); // mbaj vetem 20 veprimet e fundit
        }
    }

    public LinkedList<String> getHistory() {
        return actionHistory;
    }
}