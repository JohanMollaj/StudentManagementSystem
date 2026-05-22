import javax.swing.*;

public class testing extends JFrame {

    private JPanel mainPanel;
    private JLabel Label1;
    private JButton button1;
    private JButton button2;

    public testing() {
        setContentPane(mainPanel); // lidh formin me framen
        setTitle("Testing");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // listeners i shton ketu
        button1.addActionListener(e -> {
            Label1.setText("tested!");
        });
        button2.addActionListener(e -> {
            Label1.setText("testing!");
        });
    }
}