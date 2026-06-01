import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;

public class SearchPanel extends JPanel {
    private JTextField searchField;
    private JRadioButton nameRadio;
    private JRadioButton idRadio;
    private JRadioButton emailRadio;
    private JComboBox<String> statusCombo;
    private JButton searchButton;
    private JButton clearButton;
    private JTable resultTable;
    private JScrollPane scrollPane;

    private ArrayList<Student> allStudents = new ArrayList<>();
    private String[] columnNames = {"ID", "Emri", "Mbiemri", "Email", "Statusi"};

    public SearchPanel() {
        setLayout(new BorderLayout(10, 10));

        // paneli i kerkimit
        JPanel searchPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Kerko Studente"));

        searchPanel.add(new JLabel("Termi i kerkimit:"));
        searchField = new JTextField();
        searchPanel.add(searchField);

        searchPanel.add(new JLabel("Kerko sipas:"));
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        nameRadio = new JRadioButton("Emrit", true);
        idRadio = new JRadioButton("ID");
        emailRadio = new JRadioButton("Email");
        ButtonGroup group = new ButtonGroup();
        group.add(nameRadio);
        group.add(idRadio);
        group.add(emailRadio);
        radioPanel.add(nameRadio);
        radioPanel.add(idRadio);
        radioPanel.add(emailRadio);
        searchPanel.add(radioPanel);

        searchPanel.add(new JLabel("Filtro sipas statusit:"));
        statusCombo = new JComboBox<>(new String[]{"Te gjithe", "Aktiv", "Joaktiv"});
        searchPanel.add(statusCombo);

        // butonat
        JPanel buttonPanel = new JPanel(new FlowLayout());
        searchButton = new JButton("Kerko");
        clearButton = new JButton("Pastro");
        buttonPanel.add(searchButton);
        buttonPanel.add(clearButton);

        // tabela
        resultTable = new JTable();
        scrollPane = new JScrollPane(resultTable);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // kerkim ne kohe reale
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { performSearch(); }
            public void removeUpdate(DocumentEvent e) { performSearch(); }
            public void changedUpdate(DocumentEvent e) { performSearch(); }
        });

        searchButton.addActionListener(e -> performSearch());
        clearButton.addActionListener(e -> clearSearch());
        statusCombo.addActionListener(e -> performSearch());
    }

    private void performSearch() {
        String term = searchField.getText().toLowerCase();
        String status = statusCombo.getSelectedItem().toString();
        ArrayList<Student> results = new ArrayList<>();

        for (Student s : allStudents) {
            boolean matchesTerm;
            if (idRadio.isSelected()) {
                matchesTerm = s.getStudentId().toLowerCase().contains(term);
            } else if (emailRadio.isSelected()) {
                matchesTerm = s.getEmail().toLowerCase().contains(term);
            } else {
                matchesTerm = s.getFullName().toLowerCase().contains(term);
            }

            boolean matchesStatus = status.equals("Te gjithe") || s.getStatus().equals(status);

            if (matchesTerm && matchesStatus) {
                results.add(s);
            }
        }
        refreshTable(results);
    }

    private void clearSearch() {
        searchField.setText("");
        statusCombo.setSelectedIndex(0);
        nameRadio.setSelected(true);
        refreshTable(allStudents);
    }

    public void setStudentData(ArrayList<Student> students) {
        this.allStudents = students;
        refreshTable(students);
    }

    private void refreshTable(ArrayList<Student> list) {
        String[][] data = new String[list.size()][5];
        for (int i = 0; i < list.size(); i++) {
            Student s = list.get(i);
            data[i][0] = s.getStudentId();
            data[i][1] = s.getFirstName();
            data[i][2] = s.getLastName();
            data[i][3] = s.getEmail();
            data[i][4] = s.getStatus();
        }
        resultTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }
}