
import util.DialogHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa StudentPanel - paneli kryesor për menaxhimin e studentëve.
 * Shfaqet si një tab brenda MainFrame.
 *
 * Ka:
 *   - JTable me listën e studentëve
 *   - Fushat për të dhënat (emër, mbiemër, email, ID, status)
 *   - 4 butona: Shto, Modifiko, Fshi, Rifresko
 *
 * Komunikon me StudentManager për operacione CRUD.
 */
public class StudentPanel extends JPanel {

    // Tabela dhe modeli i saj
    private JTable table;
    private DefaultTableModel tableModel; // DefaultTableModel menaxhon të dhënat e tabelës

    // Fushat e formularit (të thjeshta, vetëm për lexim/kërkim të shpejtë)
    private JTextField txtId;
    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtEmail;
    private JComboBox<String> cmbStatus;

    // Butonat
    private JButton btnAdd;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnRefresh;

    // Referenca tek StudentManager (menaxhon listën e studentëve)
    // Do ta lidhim me bazën e të dhënave në të ardhmen
    private StudentManager studentManager;

    /**
     * Konstruktori - krijon panelin dhe inicializon të gjitha komponentet.
     */
    public StudentPanel(StudentManager studentManager) {
        this.studentManager = studentManager;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initComponents();
        layoutComponents();
        refreshTable(); // ngarko nga databaza
    }

    /**
     * Krijon të gjithë komponentet e panelit.
     */
    private void initComponents() {
        // --- Tabela ---
        // Kolonat e tabelës
        String[] columnNames = {"ID", "Emri", "Mbiemri", "Email", "Mosha", "Statusi"};

        // DefaultTableModel(kolonat, 0 rreshta fillestare)
        // false = qelizat nuk mund të ndryshohen direkt nga përdoruesi
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // asnjë qelizë nuk mund të editohet direkt
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // zgjidhim vetëm 1 rresht
        table.setRowHeight(22);
        table.getTableHeader().setReorderingAllowed(false); // kolonat nuk lëvizin

        // Kur klikohet një rresht → mbush fushat me të dhënat e atij studenti
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fillFieldsFromSelectedRow();
            }
        });

        // --- Fushat e formularit ---
        txtId        = new JTextField(15);
        txtFirstName = new JTextField(15);
        txtLastName  = new JTextField(15);
        txtEmail     = new JTextField(15);
        cmbStatus    = new JComboBox<>(new String[]{"Aktiv", "Joaktiv"});

        // --- Butonat ---
        btnAdd     = new JButton("➕ Shto");
        btnEdit    = new JButton("✏ Modifiko");
        btnDelete  = new JButton("🗑 Fshi");
        btnRefresh = new JButton("🔄 Rifresko");

        // Ngjyra e butonit Fshi → e kuqe, për paralajmërim vizual
        btnDelete.setBackground(new Color(220, 53, 69));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setOpaque(true);

        // Lidhim butonat me metodat
        btnAdd.addActionListener(e -> openAddDialog());
        btnEdit.addActionListener(e -> saveEditFromFields());
        btnDelete.addActionListener(e -> deleteSelectedStudent());
        btnRefresh.addActionListener(e -> refreshTable());
    }

    /**
     * Vendos komponentet brenda panelit.
     * BorderLayout: tabela në qendër, formulari në jug (poshtë).
     */
    private void layoutComponents() {
        // --- Paneli i sipërm: tabela ---
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista e Studentëve"));
        add(scrollPane, BorderLayout.CENTER);

        // --- Paneli i poshtëm: fushat + butonat ---
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));

        // Paneli me fushat (GridLayout 2 kolona)
        JPanel fieldsPanel = new JPanel(new GridLayout(3, 4, 5, 5));
        fieldsPanel.setBorder(BorderFactory.createTitledBorder("Të Dhënat"));

        fieldsPanel.add(new JLabel("ID:"));
        fieldsPanel.add(txtId);
        fieldsPanel.add(new JLabel("Emri:"));
        fieldsPanel.add(txtFirstName);

        fieldsPanel.add(new JLabel("Mbiemri:"));
        fieldsPanel.add(txtLastName);
        fieldsPanel.add(new JLabel("Email:"));
        fieldsPanel.add(txtEmail);

        fieldsPanel.add(new JLabel("Statusi:"));
        fieldsPanel.add(cmbStatus);
        fieldsPanel.add(new JLabel("")); // hapësirë bosh
        fieldsPanel.add(new JLabel(""));

        // Paneli me butonat
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        btnPanel.add(btnAdd);
        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);
        btnPanel.add(btnRefresh);

        bottomPanel.add(fieldsPanel, BorderLayout.CENTER);
        bottomPanel.add(btnPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Hap dialog-un për shtimin e një studenti të ri.
     * Krijon StudentDialog, pret mbylljen, pastaj shton studentin nëse u ruajt.
     */
    private void openAddDialog() {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        StudentDialog dialog = new StudentDialog(parent, "Shto Student të Ri", false);
        dialog.setVisible(true);

        Student newStudent = dialog.getSavedStudent();
        if (newStudent != null) {
            boolean success = studentManager.addStudent(newStudent);
            if (!success) {
                DialogHelper.showError("ID '" + newStudent.getStudentId() + "' ekziston tashmë!");
                return;
            }
            refreshTable();
            DialogHelper.showSuccess("Studenti '" + newStudent.getFullName() + "' u shtua me sukses!");
        }
    }

    /**
     * Hap dialog-un për modifikimin e studentit të zgjedhur.
     * Nëse nuk është zgjedhur asnjë rresht → tregon paralajmërim.
     */
    private void saveEditFromFields() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            DialogHelper.showWarning("Ju lutem zgjidhni një student nga tabela!");
            return;
        }

        // merr te dhenat nga fushat e poshtme
        String id        = txtId.getText().trim();
        String firstName = txtFirstName.getText().trim();
        String lastName  = txtLastName.getText().trim();
        String email     = txtEmail.getText().trim();
        String status    = cmbStatus.getSelectedItem().toString();

        // validim i fushave bosh
        if (id.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
            DialogHelper.showError("Ju lutem plotesoni te gjitha fushat!");
            return;
        }

        // merr studentin ekzistues per te ruajtur te dhenat qe nuk ndryshojne (birthDate, gender)
        Student existing = studentManager.getAllStudents().get(selectedRow);

        Student updated = new Student(
                id,
                firstName,
                lastName,
                email,
                existing.getBirthDate(),
                status,
                existing.getGender()
        );

        studentManager.updateStudent(updated);
        refreshTable();
        clearFields();
        table.clearSelection();
        DialogHelper.showSuccess("Studenti u modifikua me sukses!");
    }

    private void clearFields() {
        txtId.setText("");
        txtFirstName.setText("");
        txtLastName.setText("");
        txtEmail.setText("");
        cmbStatus.setSelectedIndex(0);
    }

    /**
     * Fshin studentin e zgjedhur pas konfirmimit të përdoruesit.
     */
    private void deleteSelectedStudent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            DialogHelper.showWarning("Ju lutem zgjidhni një student për të fshirë!");
            return;
        }

        Student student = studentManager.getAllStudents().get(selectedRow);
        boolean confirmed = DialogHelper.showConfirm(
                "A jeni i sigurt që doni të fshini studentin:\n'" + student.getFullName() + "'?"
        );

        if (confirmed) {
            studentManager.deleteStudent(student.getStudentId());
            refreshTable();
            clearFields();
            DialogHelper.showSuccess("Studenti u fshi me sukses!");
        }
    }

    /**
     * Rifreskon tabelën duke ri-ngarkuar të dhënat nga lista.
     * Pastron tabelën dhe ri-shton çdo student.
     */
    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Student s : studentManager.getAllStudents()) {
            addStudentToTable(s);
        }
    }

    /**
     * Shton një student si rresht të ri në tabelë.
     *
     * @param student  studenti që do të shtohet
     */
    private void addStudentToTable(Student student) {
        // Secili element i array-t korrespondon me një kolonë
        Object[] rowData = {
                student.getStudentId(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.calculateAge() + " vjeç",
                student.getStatus()
        };
        tableModel.addRow(rowData);
    }

    /**
     * Kur klikohet një rresht në tabelë, mbush fushat me të dhënat e atij studenti.
     * Kjo lejon shikimin e shpejtë pa hapër dialog.
     */
    private void fillFieldsFromSelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1 || selectedRow >= studentManager.getAllStudents().size()) return;

        Student student = studentManager.getAllStudents().get(selectedRow);
        txtId.setText(student.getStudentId());
        txtFirstName.setText(student.getFirstName());
        txtLastName.setText(student.getLastName());
        txtEmail.setText(student.getEmail());
        cmbStatus.setSelectedItem(student.getStatus());
    }
}