import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class EnrollmentPanel extends JPanel {

    private JComboBox<Course> courseCombo;
    private JTable enrolledTable;
    private JTable availableTable;
    private DefaultTableModel enrolledModel;
    private DefaultTableModel availableModel;
    private JButton enrollButton;
    private JButton unenrollButton;

    private EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
    private StudentDAO studentDAO = new StudentDAO();
    private CourseDAO courseDAO = new CourseDAO();

    private String[] studentColumns = {"ID", "Emri", "Mbiemri", "Email"};

    public EnrollmentPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initTopBar();
        initTables();
        initButtons();
        loadCourses();
    }

    private void initTopBar() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        topPanel.setBorder(BorderFactory.createTitledBorder("Zgjidh Kursin"));

        topPanel.add(new JLabel("Kursi:"));
        courseCombo = new JComboBox<>();
        courseCombo.setPreferredSize(new Dimension(250, 25));
        courseCombo.addActionListener(e -> loadStudentTables());
        topPanel.add(courseCombo);

        JButton refreshBtn = new JButton("🔄 Rifresko");
        refreshBtn.addActionListener(e -> { loadCourses(); loadStudentTables(); });
        topPanel.add(refreshBtn);

        add(topPanel, BorderLayout.NORTH);
    }

    private void initTables() {
        // tabela e majte - studentet e regjistruar
        enrolledModel = new DefaultTableModel(studentColumns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        enrolledTable = new JTable(enrolledModel);
        enrolledTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        enrolledTable.setRowHeight(22);
        JScrollPane enrolledScroll = new JScrollPane(enrolledTable);
        enrolledScroll.setBorder(BorderFactory.createTitledBorder("✅ Studentë të Regjistruar"));

        // tabela e djathte - studentet e disponueshem
        availableModel = new DefaultTableModel(studentColumns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        availableTable = new JTable(availableModel);
        availableTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        availableTable.setRowHeight(22);
        JScrollPane availableScroll = new JScrollPane(availableTable);
        availableScroll.setBorder(BorderFactory.createTitledBorder("👥 Studentë të Disponueshëm"));

        // vendos te dyja tabelat anej per anej
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT, enrolledScroll, availableScroll
        );
        splitPane.setResizeWeight(0.5); // ndaje hapesiren ne dysh
        splitPane.setDividerLocation(0.5);

        add(splitPane, BorderLayout.CENTER);
    }

    private void initButtons() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        enrollButton = new JButton("⬅ Regjistro në Kurs");
        enrollButton.setBackground(new Color(40, 167, 69));
        enrollButton.setForeground(Color.WHITE);
        enrollButton.setOpaque(true);

        unenrollButton = new JButton("➡ Hiq nga Kursi");
        unenrollButton.setBackground(new Color(220, 53, 69));
        unenrollButton.setForeground(Color.WHITE);
        unenrollButton.setOpaque(true);

        enrollButton.addActionListener(e -> enrollStudent());
        unenrollButton.addActionListener(e -> unenrollStudent());

        buttonPanel.add(unenrollButton);
        buttonPanel.add(enrollButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void loadCourses() {
        courseCombo.removeAllItems();
        for (Course c : courseDAO.getAll()) {
            courseCombo.addItem(c);
        }
    }

    void loadStudentTables() {
        enrolledModel.setRowCount(0);
        availableModel.setRowCount(0);

        if (courseCombo.getSelectedItem() == null) return;

        Course selectedCourse = (Course) courseCombo.getSelectedItem();
        ArrayList<String> enrolledIds = enrollmentDAO.getStudentsByCourse(selectedCourse.getCourseId());

        for (Student s : studentDAO.getAll()) {
            Object[] row = {s.getStudentId(), s.getFirstName(), s.getLastName(), s.getEmail()};
            if (enrolledIds.contains(s.getStudentId())) {
                enrolledModel.addRow(row); // shko ne tabelen e majte
            } else {
                availableModel.addRow(row); // shko ne tabelen e djathte
            }
        }
    }

    private void enrollStudent() {
        if (courseCombo.getSelectedItem() == null) return;
        int row = availableTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Zgjidh nje student nga lista e disponueshem!",
                    "Gabim", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String studentId = availableModel.getValueAt(row, 0).toString();
        Course course = (Course) courseCombo.getSelectedItem();

        boolean success = enrollmentDAO.enroll(studentId, course.getCourseId());
        if (success) {
            loadStudentTables(); // rifresko te dyja tabelat
        }
    }

    private void unenrollStudent() {
        if (courseCombo.getSelectedItem() == null) return;
        int row = enrolledTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Zgjidh nje student nga lista e regjistruar!",
                    "Gabim", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String studentId = enrolledModel.getValueAt(row, 0).toString();
        Course course = (Course) courseCombo.getSelectedItem();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Je i sigurt qe do heqesh kete student nga kursi?",
                "Konfirmo", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            enrollmentDAO.unenroll(studentId, course.getCourseId());
            loadStudentTables();
        }
    }
}