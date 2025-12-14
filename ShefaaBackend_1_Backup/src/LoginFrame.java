import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> userTypeBox;
    private final JButton loginButton;
    private final JButton signUpButton;

    public LoginFrame() {
        setTitle("Shefaa Hospital - Login");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        BackgroundPanel bgPanel = new BackgroundPanel();
        bgPanel.setLayout(null);

        // Logo
        JLabel logo = new JLabel("Shefaa Hospital");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        logo.setForeground(new Color(20, 60, 90));
        logo.setBounds(20, 10, 300, 40);
        bgPanel.add(logo);

        // Panel for form
        JPanel formPanel = new JPanel(null);
        formPanel.setBounds(150, 80, 300, 220);
        formPanel.setOpaque(false);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setBounds(0, 0, 100, 25);
        formPanel.add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(0, 25, 300, 30);
        formPanel.add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passLabel.setBounds(0, 60, 100, 25);
        formPanel.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(0, 85, 300, 30);
        formPanel.add(passwordField);

        JLabel typeLabel = new JLabel("User Type:");
        typeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        typeLabel.setBounds(0, 120, 100, 25);
        formPanel.add(typeLabel);

        userTypeBox = new JComboBox<>(new String[]{"Admin", "Doctor", "Patient"});
        userTypeBox.setBounds(0, 145, 300, 30);
        formPanel.add(userTypeBox);

        loginButton = new JButton("Login");
        loginButton.setBounds(0, 185, 140, 30);
        styleButton(loginButton);
        formPanel.add(loginButton);

        signUpButton = new JButton("Sign Up");
        signUpButton.setBounds(160, 185, 140, 30);
        styleButton(signUpButton);
        formPanel.add(signUpButton);

        // Action Events
        loginButton.addActionListener((ActionEvent e) -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String userType = userTypeBox.getSelectedItem().toString().toLowerCase();

            if (LoginService.validateLogin(username, password, userType)) {
                JOptionPane.showMessageDialog(this, "✅ Login Successful!");

                switch (userType) {
                    case "admin":
                        new AdminLoginFrame().setVisible(true);
                        break;
                    case "doctor":
                        new DoctorLoginFrame().setVisible(true);
                        break;
                    case "patient":
                        new PatientDashboard(username).setVisible(true);
                        break;
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Invalid Credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        signUpButton.addActionListener(e -> new SignUpFrame().setVisible(true));

        bgPanel.add(formPanel);
        setContentPane(bgPanel);
        setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(new Color(30, 144, 255));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

public static void main(String[] args) {
    // عرض نافذة الشعار
    JWindow splashWindow = new JWindow();
    splashWindow.setSize(800, 600);
    splashWindow.setLocationRelativeTo(null);

    ImageIcon splashLogo = new ImageIcon(LoginFrame.class.getResource("/logo.png"));
    JLabel splashLabel = new JLabel(splashLogo);
    splashWindow.getContentPane().add(splashLabel, BorderLayout.CENTER);

    splashWindow.setVisible(true);

    try {
        Thread.sleep(5000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }

    splashWindow.dispose();

    // فتح واجهة تسجيل الدخول
    SwingUtilities.invokeLater(LoginFrame::new);
}
}
