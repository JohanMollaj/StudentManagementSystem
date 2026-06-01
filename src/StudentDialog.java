
import util.Validator;
import util.DialogHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Klasa StudentDialog - dritare popup për shtim ose modifikim studenti.
 * Shfaqet si dialog mbi MainFrame dhe ka formular me fushat e studentit.
 *
 * Thirret nga StudentPanel kur klikon "Shto" ose "Modifiko".
 */
public class StudentDialog extends JDialog {

    // Fushat e formularit
    private JTextField txtId;          // fusha për ID
    private JTextField txtFirstName;   // fusha për emër
    private JTextField txtLastName;    // fusha për mbiemër
    private JTextField txtEmail;       // fusha për email
    private JTextField txtBirthDate;   // fusha për datëlindje (format: yyyy-MM-dd)
    private JComboBox<String> cmbStatus; // zgjedhja e statusit
    private JRadioButton rdMale;       // butoni radio për mashkull
    private JRadioButton rdFemale;     // butoni radio për femër

    // Butona
    private JButton btnSave;
    private JButton btnCancel;

    // Studenti i ruajtur pas klikimit të "Ruaj" (null nëse klikoi Anulo)
    private Student savedStudent;

    // Boolean - a është ky dialog për modifikim (true) apo shtim (false)?
    private boolean isEditMode;

    /**
     * Konstruktori - krijon dialog-un.
     *
     * @param parent    dritarja prindër (zakonisht MainFrame)
     * @param title     titulli i dialog-ut
     * @param isEdit    true = jemi duke modifikuar; false = jemi duke shtuar
     */
    public StudentDialog(JFrame parent, String title, boolean isEdit) {
        // super thirr konstruktorin e JDialog
        // true = dialog modal (bllokojmë prindërin derisa të mbyllet)
        super(parent, title, true);
        this.isEditMode = isEdit;

        initComponents();  // krijon komponentet vizuale
        layoutComponents(); // i vendos ato në panel

        // Dimensionet dhe vendosja e dialog-ut
        setSize(400, 380);
        setLocationRelativeTo(parent); // qendër mbi prindërin
        setResizable(false);
    }

    /**
     * Krijon të gjithë komponentet vizuale (fushat, butonat, etj.).
     */
    private void initComponents() {
        txtId        = new JTextField(20);
        txtFirstName = new JTextField(20);
        txtLastName  = new JTextField(20);
        txtEmail     = new JTextField(20);
        txtBirthDate = new JTextField(20);
        txtBirthDate.setToolTipText("Formati: yyyy-MM-dd  (p.sh. 2000-05-15)");

        // ComboBox me opsionet e statusit
        cmbStatus = new JComboBox<>(new String[]{"Aktiv", "Joaktiv"});

        // RadioButton për gjininë
        rdMale   = new JRadioButton("Mashkull");
        rdFemale = new JRadioButton("Femër");
        rdMale.setSelected(true); // zgjidhim mashkull si parazgjedhje

        // ButtonGroup siguron që vetëm njëri RadioButton është i zgjedhur njëherësh
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(rdMale);
        genderGroup.add(rdFemale);

        btnSave   = new JButton("💾 Ruaj");
        btnCancel = new JButton("✖ Anulo");

        // Kur klikohet "Ruaj" → thirrnim metodën saveStudent()
        btnSave.addActionListener((ActionEvent e) -> saveStudent());

        // Kur klikohet "Anulo" → mbyllim dialog-un pa ruajtur asgjë
        btnCancel.addActionListener((ActionEvent e) -> dispose());
    }

    /**
     * Vendos komponentet në panel me GridBagLayout.
     * GridBagLayout lejon vendosje të saktë të çdo komponenti.
     */
    private void layoutComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 4, 4, 4); // hapësirë rreth çdo komponenti

        // Rreshti 0: ID
        addRow(panel, gbc, 0, "ID Studenti:", txtId);

        // Rreshti 1: Emri
        addRow(panel, gbc, 1, "Emri:", txtFirstName);

        // Rreshti 2: Mbiemri
        addRow(panel, gbc, 2, "Mbiemri:", txtLastName);

        // Rreshti 3: Email
        addRow(panel, gbc, 3, "Email:", txtEmail);

        // Rreshti 4: Datëlindja
        addRow(panel, gbc, 4, "Datëlindja:", txtBirthDate);

        // Rreshti 5: Statusi
        addRow(panel, gbc, 5, "Statusi:", cmbStatus);

        // Rreshti 6: Gjinia (dy RadioButton)
        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(new JLabel("Gjinia:"), gbc);
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        genderPanel.add(rdMale);
        genderPanel.add(rdFemale);
        gbc.gridx = 1; gbc.gridy = 6;
        panel.add(genderPanel, gbc);

        // Rreshti 7: Butonat Ruaj dhe Anulo
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        gbc.gridx = 0; gbc.gridy = 7;
        gbc.gridwidth = 2; // butoni zë dy kolona
        panel.add(btnPanel, gbc);

        // Shto panelin në dialog
        add(panel);
    }

    /**
     * Metodë ndihmëse: shton një rresht (etiketë + fushë) në panel.
     *
     * @param panel   paneli ku i shtojmë
     * @param gbc     konfigurimi i GridBag
     * @param row     numri i rreshtit
     * @param label   teksti i etiketës
     * @param field   komponenti i fushës
     */
    private void addRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = row;
        gbc.weightx = 0.3;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        gbc.weightx = 0.7;
        panel.add(field, gbc);
    }

    /**
     * Mbush fushat e formularit me të dhënat e një studenti ekzistues.
     * Thirret nga StudentPanel kur hap dialog-un për modifikim.
     *
     * @param student  studenti i zgjedhur nga tabela
     */
    public void fillFields(Student student) {
        txtId.setText(student.getStudentId());
        txtId.setEditable(false); // ID nuk ndryshohet gjatë modifikimit
        txtFirstName.setText(student.getFirstName());
        txtLastName.setText(student.getLastName());
        txtEmail.setText(student.getEmail());

        // Shndërrojmë LocalDate në tekst për fushën
        if (student.getBirthDate() != null) {
            txtBirthDate.setText(student.getBirthDate().toString());
        }

        cmbStatus.setSelectedItem(student.getStatus());

        // Zgjedhim butonin e duhur të gjinisë
        if ("Femër".equals(student.getGender())) {
            rdFemale.setSelected(true);
        } else {
            rdMale.setSelected(true);
        }
    }

    /**
     * Lexon të dhënat nga fushat, i validon, dhe krijon objektin Student.
     * Nëse ka gabim → tregon mesazh dhe nuk e mbyll dialog-un.
     * Nëse gjithçka është mirë → ruan studentin dhe mbyll dialog-un.
     */
    private void saveStudent() {
        // Lexojmë vlerat nga fushat
        String id        = txtId.getText().trim();
        String firstName = txtFirstName.getText().trim();
        String lastName  = txtLastName.getText().trim();
        String email     = txtEmail.getText().trim();
        String birthStr  = txtBirthDate.getText().trim();
        String status    = (String) cmbStatus.getSelectedItem();
        String gender    = rdFemale.isSelected() ? "Femër" : "Mashkull";

        // Validojmë çdo fushë. Nëse ndonjë dështon → dalim (return)
        if (!Validator.isValidId(id))                       return;
        if (!Validator.isEmptyField(firstName, "Emri"))     return;
        if (!Validator.isEmptyField(lastName, "Mbiemri"))   return;
        if (!Validator.isValidEmail(email))                 return;

        // Provojmë ta kthejmë datën nga tekst në LocalDate
        LocalDate birthDate = null;
        if (!birthStr.isEmpty()) {
            try {
                // DateTimeFormatter.ISO_DATE pret formatin: yyyy-MM-dd
                birthDate = LocalDate.parse(birthStr, DateTimeFormatter.ISO_DATE);
            } catch (DateTimeParseException ex) {
                DialogHelper.showError("Data e lindjes nuk është e vlefshme!\nPërdor formatin: yyyy-MM-dd (p.sh. 2000-05-15)");
                return;
            }
        }

        // Krijojmë objektin e studentit
        savedStudent = new Student(id, firstName, lastName, email, birthDate, status, gender);

        // Mbyllim dialog-un
        dispose();
    }

    /**
     * Kthen studentin e ruajtur.
     * Thirret nga StudentPanel pas mbylljes së dialog-ut.
     *
     * @return studenti i ri/modifikuar, ose null nëse klikoi Anulo
     */
    public Student getSavedStudent() {
        return savedStudent;
    }
}