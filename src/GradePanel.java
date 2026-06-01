import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GradePanel extends JPanel {
    private JComboBox<String> studentCombo;
    private JComboBox<String> courseCombo;
    private JTextField gradeField;
    private JTextField semesterField;
    private JLabel letterGradeLabel;
    private JButton addGradeButton;
    private JButton deleteGradeButton;
    private JTable gradeTable;
    private JScrollPane scrollPane;

    private ArrayList<Grade> gradeList = new ArrayList<>();
    private String[] columnNames = {"ID", "Student ID", "Kurs ID", "Nota", "Shkronja", "Semestri"};

    public GradePanel() {
        setLayout(new BorderLayout(10, 10));

        // paneli i siperm
        JPanel fieldsPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        fieldsPanel.setBorder(BorderFactory.createTitledBorder("Shto Note"));

        fieldsPanel.add(new JLabel("Studenti:"));
        studentCombo = new JComboBox<>(new String[]{"S001 - Andi Malaj", "S002 - Sara Domi", "S003 - Ledi Cela"});
        fieldsPanel.add(studentCombo);

        fieldsPanel.add(new JLabel("Kursi:"));
        courseCombo = new JComboBox<>(new String[]{"C001 - Matematike", "C002 - Programim", "C003 - Fizike"});
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

        // update letra automatikisht ndersa shkruan noten
        gradeField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateLetter(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateLetter(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateLetter(); }
        });

        // butonat
        JPanel buttonPanel = new JPanel(new FlowLayout());
        addGradeButton = new JButton("Shto Note");
        deleteGradeButton = new JButton("Fshi Note");
        buttonPanel.add(addGradeButton);
        buttonPanel.add(deleteGradeButton);

        // tabela
        gradeTable = new JTable();
        scrollPane = new JScrollPane(gradeTable);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(fieldsPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // listeners
        addGradeButton.addActionListener(e -> addGrade());
        deleteGradeButton.addActionListener(e -> deleteGrade());
    }

    private void updateLetter() {
        try {
            double g = Double.parseDouble(gradeField.getText());
            Grade temp = new Grade(0, "", "", g, "");
            letterGradeLabel.setText(temp.getLetterGrade());
        } catch (NumberFormatException ex) {
            letterGradeLabel.setText("-");
        }
    }

    private void addGrade() {
        try {
            if (gradeField.getText().isEmpty() || semesterField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nota dhe semestri jane te detyrueshme!", "Gabim", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double g = Double.parseDouble(gradeField.getText());
            if (g < 0 || g > 10) {
                JOptionPane.showMessageDialog(this, "Nota duhet te jete ndermjet 0 dhe 10!", "Gabim", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String studentId = studentCombo.getSelectedItem().toString().split(" - ")[0];
            String courseId = courseCombo.getSelectedItem().toString().split(" - ")[0];
            Grade grade = new Grade(gradeList.size() + 1, studentId, courseId, g, semesterField.getText());
            gradeList.add(grade);
            refreshTable();
            gradeField.setText("");
            semesterField.setText("");
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
        int confirm = JOptionPane.showConfirmDialog(this, "Je i sigurt qe do fshish kete note?", "Konfirmo", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            gradeList.remove(row);
            refreshTable();
        }
    }

    private void refreshTable() {
        String[][] data = new String[gradeList.size()][6];
        for (int i = 0; i < gradeList.size(); i++) {
            Grade g = gradeList.get(i);
            data[i][0] = String.valueOf(g.getGradeId());
            data[i][1] = g.getStudentId();
            data[i][2] = g.getCourseId();
            data[i][3] = String.valueOf(g.getGrade());
            data[i][4] = g.getLetterGrade();
            data[i][5] = g.getSemester();
        }
        gradeTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }
}