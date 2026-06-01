import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.ArrayList;

public class CoursePanel extends JPanel {
    private JTextField courseIdField;
    private JTextField courseNameField;
    private JTextField creditsField;
    private JTextField lecturerField;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JTable courseTable;
    private JScrollPane scrollPane;

    private ArrayList<Course> courseList = new ArrayList<>();
    private String[] columnNames = {"ID", "Emri Kursit", "Kredite", "Ligjëruesi"};

    public CoursePanel() {
        setLayout(new BorderLayout(10, 10));

        // paneli i siperm per fushat
        JPanel fieldsPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        fieldsPanel.setBorder(BorderFactory.createTitledBorder("Te dhenat e kursit"));

        fieldsPanel.add(new JLabel("Course ID:"));
        courseIdField = new JTextField();
        fieldsPanel.add(courseIdField);

        fieldsPanel.add(new JLabel("Emri Kursit:"));
        courseNameField = new JTextField();
        fieldsPanel.add(courseNameField);

        fieldsPanel.add(new JLabel("Kredite:"));
        creditsField = new JTextField();
        fieldsPanel.add(creditsField);

        fieldsPanel.add(new JLabel("Ligjëruesi:"));
        lecturerField = new JTextField();
        fieldsPanel.add(lecturerField);

        // paneli i butonave
        JPanel buttonPanel = new JPanel(new FlowLayout());
        addButton = new JButton("Shto");
        updateButton = new JButton("Modifiko");
        deleteButton = new JButton("Fshi");
        refreshButton = new JButton("Rifresko");
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        // tabela
        courseTable = new JTable();
        scrollPane = new JScrollPane(courseTable);

        // vendos ne panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(fieldsPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // kur zgjidhet nje rresht mbush fushat
        courseTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && courseTable.getSelectedRow() != -1) {
                int row = courseTable.getSelectedRow();
                courseIdField.setText(courseTable.getValueAt(row, 0).toString());
                courseNameField.setText(courseTable.getValueAt(row, 1).toString());
                creditsField.setText(courseTable.getValueAt(row, 2).toString());
                lecturerField.setText(courseTable.getValueAt(row, 3).toString());
            }
        });

        // listeners per butonat
        addButton.addActionListener(e -> addCourse());
        updateButton.addActionListener(e -> updateCourse());
        deleteButton.addActionListener(e -> deleteCourse());
        refreshButton.addActionListener(e -> refreshTable());
    }

    private void addCourse() {
        try {
            if (courseIdField.getText().isEmpty() || courseNameField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID dhe Emri jane te detyrueshme!", "Gabim", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int credits = Integer.parseInt(creditsField.getText());
            Course c = new Course(courseIdField.getText(), courseNameField.getText(), credits, lecturerField.getText());
            courseList.add(c);
            refreshTable();
            clearFields();
            JOptionPane.showMessageDialog(this, "Kursi u shtua me sukses!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Kreditet duhet te jene numer!", "Gabim", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCourse() {
        int row = courseTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Zgjidh nje kurs per modifikim!", "Gabim", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            int credits = Integer.parseInt(creditsField.getText());
            courseList.get(row).setCourseName(courseNameField.getText());
            courseList.get(row).setCredits(credits);
            courseList.get(row).setLecturer(lecturerField.getText());
            refreshTable();
            JOptionPane.showMessageDialog(this, "Kursi u modifikua me sukses!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Kreditet duhet te jene numer!", "Gabim", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteCourse() {
        int row = courseTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Zgjidh nje kurs per fshirje!", "Gabim", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Je i sigurt qe do fshish kete kurs?", "Konfirmo", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            courseList.remove(row);
            refreshTable();
            clearFields();
        }
    }

    private void refreshTable() {
        String[][] data = new String[courseList.size()][4];
        for (int i = 0; i < courseList.size(); i++) {
            Course c = courseList.get(i);
            data[i][0] = c.getCourseId();
            data[i][1] = c.getCourseName();
            data[i][2] = String.valueOf(c.getCredits());
            data[i][3] = c.getLecturer();
        }
        courseTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }

    private void clearFields() {
        courseIdField.setText("");
        courseNameField.setText("");
        creditsField.setText("");
        lecturerField.setText("");
    }
}