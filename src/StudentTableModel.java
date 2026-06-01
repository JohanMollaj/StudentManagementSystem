import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class StudentTableModel extends AbstractTableModel {
    private final String[] columns = {"ID", "Emri", "Mbiemri", "Email", "Mosha", "Statusi"};
    private ArrayList<Student> studentList = new ArrayList<>();

    @Override
    public int getRowCount() {
        return studentList.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int col) {
        return columns[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        Student s = studentList.get(row);
        switch (col) {
            case 0: return s.getStudentId();
            case 1: return s.getFirstName();
            case 2: return s.getLastName();
            case 3: return s.getEmail();
            case 4: return s.calculateAge();
            case 5: return s.getStatus();
            default: return null;
        }
    }

    public void setData(ArrayList<Student> list) {
        this.studentList = list;
        fireTableDataChanged(); // rifresko JTable automatikisht
    }

    public Student getStudentAt(int row) {
        return studentList.get(row);
    }
}