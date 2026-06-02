import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class GradePanel extends JPanel {
    private JComboBox<Student> studentCombo;
    private JComboBox<Course> courseCombo;
    private JTextField gradeField;
    private JTextField semesterField;
    private JLabel letterGradeLabel;
    private JButton addGradeButton;
    private JButton deleteGradeButton;
    private JButton refreshButton;
    private JTable gradeTable;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;

    private GradeDAO gradeDAO = new GradeDAO();
    private StudentDAO studentDAO = new StudentDAO();
    private CourseDAO courseDAO = new CourseDAO();

    private String[] columnNames = {"ID", "Studenti", "Kursi", "Nota", "Shkronja", "Semestri"};

    public GradePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initTable();
        initForm();
        loadCombos();
        refreshTable();
    }

    private void initTable() {
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        gradeTable = new JTable(tableModel);
        gradeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        gradeTable.setRowHeight(22);
        gradeTable.getTableHeader().setReorderingAllowed(false);

        scrollPane = new JScrollPane(gradeTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista e Notave"));
        add(scrollPane, BorderLayout.CENTER);
    }

    private void initForm() {
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));

        // fushat
        JPanel fieldsPanel = new JPanel(new GridLayout(3, 4, 5, 5));
        fieldsPanel.setBorder(BorderFactory.createTitledBorder("Shto Note"));

        fieldsPanel.add(new JLabel("Studenti:"));
        studentCombo = new JComboBox<>();
        fieldsPanel.add(studentCombo);

        fieldsPanel.add(new JLabel("Kursi:"));
        courseCombo = new JComboBox<>();
        courseCombo.addActionListener(e -> loadEnrolledStudents());
        fieldsPanel.add(courseCombo);

        fieldsPanel.add(new JLabel("Nota (0-10):"));
        gradeField = new JTextField();
        fieldsPanel.add(gradeField);

        fieldsPanel.add(new JLabel("Semestri:"));
        semesterField = new JTextField();
        fieldsPanel.add(semesterField);

        fieldsPanel.add(new JLabel("Nota me shkronje:"));
        letterGradeLabel = new JLabel("-");
        letterGradeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        fieldsPanel.add(letterGradeLabel);

        // placeholder per rresht te trete
        fieldsPanel.add(new JLabel(""));
        fieldsPanel.add(new JLabel(""));

        // update letra automatikisht ndersa shkruan
        gradeField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateLetter(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateLetter(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateLetter(); }
        });

        // butonat
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        addGradeButton    = new JButton("➕ Shto Note");
        deleteGradeButton = new JButton("🗑 Fshi Note");
        refreshButton     = new JButton("🔄 Rifresko");

        deleteGradeButton.setBackground(new Color(220, 53, 69));
        deleteGradeButton.setForeground(Color.WHITE);
        deleteGradeButton.setOpaque(true);

        buttonPanel.add(addGradeButton);
        buttonPanel.add(deleteGradeButton);
        buttonPanel.add(refreshButton);

        addGradeButton.addActionListener(e -> addGrade());
        deleteGradeButton.addActionListener(e -> deleteGrade());
        refreshButton.addActionListener(e -> {
            loadCombos();
            refreshTable();
        });

        bottomPanel.add(fieldsPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // ngarko studentet dhe kurset nga databaza ne combo boxes
    public void loadCombos() {
        studentCombo.removeAllItems();
        courseCombo.removeAllItems();

        // shfaq vetem kurset ne combo
        ArrayList<Course> courses = courseDAO.getAll();
        for (Course c : courses) courseCombo.addItem(c);
    }

    private void loadEnrolledStudents() {
        studentCombo.removeAllItems();
        if (courseCombo.getSelectedItem() == null) return;

        Course selected = (Course) courseCombo.getSelectedItem();
        EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
        ArrayList<String> enrolledIds = enrollmentDAO.getStudentsByCourse(selected.getCourseId());

        for (Student s : studentDAO.getAll()) {
            if (enrolledIds.contains(s.getStudentId())) {
                studentCombo.addItem(s);
            }
        }
    }

    private void updateLetter() {
        try {
            double g = Double.parseDouble(gradeField.getText().trim());
            Grade temp = new Grade(0, "", "", g, "");
            letterGradeLabel.setText(temp.getLetterGrade());

            // ngjyros sipas notes
            if (g >= 9)      letterGradeLabel.setForeground(new Color(0, 150, 0));   // gjelber
            else if (g >= 7) letterGradeLabel.setForeground(new Color(0, 100, 200)); // blu
            else if (g >= 5) letterGradeLabel.setForeground(new Color(200, 140, 0)); // portokalli
            else             letterGradeLabel.setForeground(new Color(220, 53, 69)); // kuq

        } catch (NumberFormatException ex) {
            letterGradeLabel.setText("-");
            letterGradeLabel.setForeground(Color.BLACK);
        }
    }

    private void addGrade() {
        if (studentCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Nuk ka studente! Shto studente fillimisht.", "Gabim", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (courseCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Nuk ka kurse! Shto kurse fillimisht.", "Gabim", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (gradeField.getText().trim().isEmpty() || semesterField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nota dhe semestri jane te detyrueshme!", "Gabim", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            double g = Double.parseDouble(gradeField.getText().trim());
            if (g < 0 || g > 10) {
                JOptionPane.showMessageDialog(this, "Nota duhet te jete ndermjet 0 dhe 10!", "Gabim", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Student selectedStudent = (Student) studentCombo.getSelectedItem();
            Course selectedCourse   = (Course) courseCombo.getSelectedItem();

            Grade grade = new Grade(0, selectedStudent.getStudentId(),
                    selectedCourse.getCourseId(), g,
                    semesterField.getText().trim());
            gradeDAO.insert(grade);
            refreshTable();
            gradeField.setText("");
            semesterField.setText("");
            letterGradeLabel.setText("-");
            JOptionPane.showMessageDialog(this, "Nota u shtua me sukses!");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Nota duhet te jete numer!", "Gabim", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteGrade() {
        int row = gradeTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Zgjidh nje note per fshirje!", "Gabim", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Je i sigurt qe do fshish kete note?", "Konfirmo", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int gradeId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
            gradeDAO.delete(gradeId);
            refreshTable();
        }
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        // ngarko te gjitha notat me join - shfaq emrat jo ID-te
        ArrayList<Student> students = studentDAO.getAll();
        ArrayList<Course> courses   = courseDAO.getAll();

        // krijo HashMap per kërkim te shpejte
        java.util.HashMap<String, String> studentNames = new java.util.HashMap<>();
        java.util.HashMap<String, String> courseNames  = new java.util.HashMap<>();

        for (Student s : students) studentNames.put(s.getStudentId(), s.getFullName());
        for (Course c : courses)   courseNames.put(c.getCourseId(), c.getCourseName());

        // merr te gjitha notat per cdo student
        for (Student s : students) {
            for (Grade g : gradeDAO.getByStudent(s.getStudentId())) {
                tableModel.addRow(new Object[]{
                        g.getGradeId(),
                        studentNames.getOrDefault(g.getStudentId(), g.getStudentId()),
                        courseNames.getOrDefault(g.getCourseId(), g.getCourseId()),
                        g.getGrade(),
                        g.getLetterGrade(),
                        g.getSemester()
                });
            }
        }
    }
}