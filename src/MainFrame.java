
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Klasa MainFrame - dritarja kryesore e aplikacionit.
 * Kjo është "skeleti" i gjithë programit: ka menu dhe tab-at kryesore.
 *
 * Struktura:
 *   MainFrame
 *   ├── JMenuBar
 *   │    ├── Menu "File"  (Fil, Dil)
 *   │    └── Menu "Help"  (Rreth)
 *   └── JTabbedPane
 *        ├── Tab "Studentë"  → StudentPanel
 *        ├── Tab "Kurse"     → placeholder (do bëhet)
 *        ├── Tab "Nota"      → placeholder (do bëhet)
 *        └── Tab "Kërkim"    → placeholder (do bëhet)
 */
public class MainFrame extends JFrame {

    // JTabbedPane - kontrolli me tab-a
    private JTabbedPane tabbedPane;

    // Panelet e tab-ave
    private StudentPanel studentPanel;
    private CoursePanel coursePanel;
    private GradePanel gradePanel;
    private EnrollmentPanel enrollmentPanel;

    // CoursePanel, GradePanel, SearchPanel do shtohen më vonë

    /**
     * Konstruktori - krijon dhe konfiguron dritaren kryesore.
     * Thirret nga klasa Main kur niset programi.
     */
    public MainFrame() {
        initWindow();    // konfiguron dritaren (titull, madhësi, etj.)
        initMenuBar();   // krijon menu-në e sipërme
        initTabs();      // krijon tab-at e panelit
    }

    /**
     * Konfiguron vetitë bazë të dritares.
     */
    private void initWindow() {
        setTitle("Sistemi i Menaxhimit të Studentëve");

        // Dimensionet e dritares: gjerësi x lartësi në pixel
        setSize(1024, 768);

        // Vendos dritaren në qendër të ekranit
        setLocationRelativeTo(null);

        // Kur mbyllim dritaren (X) → mbyllim tërë programin
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Ikonë (e vendosim nëse ekziston skedari; nëse jo, Java përdor ikonën default)
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/assets/icon.png"));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            // Nëse nuk gjendet skedari i ikonës, vazhdojmë pa të
        }
    }

    /**
     * Krijon JMenuBar me menutë File dhe Help.
     * JMenuBar vendoset në krye të dritares.
     */
    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // ===== MENU FILE =====
        JMenu menuFile = new JMenu("File");
        menuFile.setMnemonic('F'); // Alt+F hapim menunë

        // Nën-menu "Dil" → mbyll programin
        JMenuItem itemExit = new JMenuItem("Dil");
        itemExit.setAccelerator(
                KeyStroke.getKeyStroke("control Q") // Ctrl+Q shkurtore
        );
        itemExit.addActionListener((ActionEvent e) -> {
            // Pyesim para daljes
            int choice = JOptionPane.showConfirmDialog(this,
                    "A jeni i sigurt që doni të dilni?",
                    "Konfirmo Daljen",
                    JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0); // mbyllim aplikacionin
            }
        });

        menuFile.add(itemExit);

        // ===== MENU HELP =====
        JMenu menuHelp = new JMenu("Help");
        menuHelp.setMnemonic('H');

        // Nën-menu "Rreth" → info mbi programin
        JMenuItem itemAbout = new JMenuItem("Rreth Programit");
        itemAbout.addActionListener((ActionEvent e) -> {
            JOptionPane.showMessageDialog(this,
                    "Sistemi i Menaxhimit të Studentëve\n" +
                            "Versioni 1.0\n" +
                            "Zhvilluar me Java Swing",
                    "Rreth",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        menuHelp.add(itemAbout);

        // Shtojmë të dy menutë në menuBar
        menuBar.add(menuFile);
        menuBar.add(menuHelp);

        // Vendosim menuBar-in në dritare
        setJMenuBar(menuBar);
    }

    /**
     * Krijon JTabbedPane me 4 tab-a dhe inicializon panelet.
     * JTabbedPane lejon kalimin midis seksioneve të ndryshme.
     */
    private void initTabs() {
        tabbedPane = new JTabbedPane();
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 2) { // ndryshoje indexin sipas pozicionit
                enrollmentPanel.loadCourses();
                enrollmentPanel.loadStudentTables();
            }
        });
        StudentManager studentManager = new StudentManager();

        studentManager.loadAllFromDB();
        studentPanel = new StudentPanel(studentManager);
        tabbedPane.addTab("👥 Studentë", studentPanel);

        // ===== TAB 2: Kurse (placeholder) =====
        coursePanel = new CoursePanel();
        tabbedPane.addTab("📚 Kurse", coursePanel);

        enrollmentPanel = new EnrollmentPanel();
        tabbedPane.addTab("📋 Regjistrimet", enrollmentPanel);

        // ===== TAB 3: Nota (placeholder) =====
        gradePanel = new GradePanel();
        tabbedPane.addTab("📊 Nota", gradePanel);

        // Shtojmë tabbedPane në qendër të dritares
        add(tabbedPane, BorderLayout.CENTER);
    }

    /**
     * Krijon panel të thjeshtë si "placeholder" për tab-at ende pa implementim.
     * Shfaq vetëm tekst informues.
     *
     * @param message  mesazhi që do të shfaqet
     * @return paneli i krijuar
     */
    private JPanel createPlaceholderPanel(String message) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(message, JLabel.CENTER);
        label.setFont(new Font("Arial", Font.ITALIC, 16));
        label.setForeground(Color.GRAY);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }
}