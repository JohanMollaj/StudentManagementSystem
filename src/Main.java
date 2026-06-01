import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // testo lidhjen dhe inicializo tabelat para se te hapet GUI
        DBInitializer.initialize();

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}