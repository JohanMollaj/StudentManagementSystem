import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
    private JTextField txtSearch;
    private JTable courseTable;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;

    private CourseDAO courseDAO = new CourseDAO();
    private String[] columnNames = {"ID", "Emri Kursit", "Kredite", "Ligjëruesi"};

    public CoursePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initTable();
        initSearchBar();
        initForm();
        refreshTable();
    }

    private void initTable() {
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        courseTable = new JTable(tableModel);
        courseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        courseTable.setRowHeight(22);
        courseTable.getTableHeader().setReorderingAllowed(false);

        // kur zgjidhet nje rresht mbush fushat
        courseTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && courseTable.getSelectedRow() != -1) {
                int row = courseTable.getSelectedRow();
                courseIdField.setText(tableModel.getValueAt(row, 0).toString());
                courseNameField.setText(tableModel.getValueAt(row, 1).toString());
                creditsField.setText(tableModel.getValueAt(row, 2).toString());
                lecturerField.setText(tableModel.getValueAt(row, 3).toString());
            }
        });

        scrollPane = new JScrollPane(courseTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista e Kurseve"));
        add(scrollPane, BorderLayout.CENTER);
    }

    private void initSearchBar() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Kërkim"));

        searchPanel.add(new JLabel("Kërko:"));
        txtSearch = new JTextField(20);
        searchPanel.add(txtSearch);

        JButton btnClear = new JButton("Pastro");
        btnClear.addActionListener(e -> {
            txtSearch.setText("");
            refreshTable();
        });
        searchPanel.add(btnClear);

        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { performSearch(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { performSearch(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { performSearch(); }
        });

        add(searchPanel, BorderLayout.NORTH);
    }

    private void initForm() {
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));

        // fushat
        JPanel fieldsPanel = new JPanel(new GridLayout(2, 4, 5, 5));
        fieldsPanel.setBorder(BorderFactory.createTitledBorder("Te Dhenat e Kursit"));

        fieldsPanel.add(new JLabel("Course ID:"));
        courseIdField = new JTextField();
        courseIdField.setEditable(false);
        courseIdField.setBackground(new Color(230, 230, 230));
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

        // butonat
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        addButton    = new JButton("➕ Shto");
        updateButton = new JButton("✏ Modifiko");
        deleteButton = new JButton("🗑 Fshi");
        refreshButton = new JButton("🔄 Rifresko");

        deleteButton.setBackground(new Color(220, 53, 69));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setOpaque(true);

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        addButton.addActionListener(e -> addCourse());
        updateButton.addActionListener(e -> updateCourse());
        deleteButton.addActionListener(e -> deleteCourse());
        refreshButton.addActionListener(e -> refreshTable());

        bottomPanel.add(fieldsPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private String generateNextId() {
        int maxId = 0;
        for (Course c : courseDAO.getAll()) {
            try {
                int num = Integer.parseInt(c.getCourseId().substring(1));
                if (num > maxId) maxId = num;
            } catch (NumberFormatException e) { }
        }
        return String.format("C%03d", maxId + 1);
    }

    private void addCourse() {
        if (courseNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Emri i kursit eshte i detyrueshëm!", "Gabim", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            int credits = Integer.parseInt(creditsField.getText().trim());
            if (credits <= 0) {
                JOptionPane.showMessageDialog(this, "Kreditet duhet te jene me shume se 0!", "Gabim", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String id = generateNextId();
            Course c = new Course(id, courseNameField.getText().trim(), credits, lecturerField.getText().trim());
            courseDAO.insert(c);
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
        if (courseNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Emri i kursit eshte i detyrueshëm!", "Gabim", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            int credits = Integer.parseInt(creditsField.getText().trim());
            String id = tableModel.getValueAt(row, 0).toString();
            Course c = new Course(id, courseNameField.getText().trim(), credits, lecturerField.getText().trim());
            courseDAO.update(c);
            refreshTable();
            clearFields();
            courseTable.clearSelection();
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
        String id = tableModel.getValueAt(row, 0).toString();
        String name = tableModel.getValueAt(row, 1).toString();
        int confirm = JOptionPane.showConfirmDialog(this,
                "Je i sigurt qe do fshish kursin:\n'" + name + "'?",
                "Konfirmo", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            courseDAO.delete(id);
            refreshTable();
            clearFields();
            courseTable.clearSelection();
        }
    }

    private void performSearch() {
        String term = txtSearch.getText().toLowerCase().trim();
        if (term.isEmpty()) { refreshTable(); return; }

        tableModel.setRowCount(0);
        for (Course c : courseDAO.getAll()) {
            if (c.getCourseName().toLowerCase().contains(term) ||
                    c.getCourseId().toLowerCase().contains(term) ||
                    c.getLecturer().toLowerCase().contains(term)) {
                tableModel.addRow(new Object[]{
                        c.getCourseId(), c.getCourseName(),
                        c.getCredits(), c.getLecturer()
                });
            }
        }
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        for (Course c : courseDAO.getAll()) {
            tableModel.addRow(new Object[]{
                    c.getCourseId(), c.getCourseName(),
                    c.getCredits(), c.getLecturer()
            });
        }
    }

    private void clearFields() {
        courseIdField.setText("");
        courseNameField.setText("");
        creditsField.setText("");
        lecturerField.setText("");
    }
}