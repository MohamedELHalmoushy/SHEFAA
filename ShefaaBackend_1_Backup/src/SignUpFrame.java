import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SignUpFrame extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JTextField phoneField;
    private final JTextField addressField;
    private final JComboBox<String> genderComboBox;
    private final JTextField ageField;
    private final JComboBox<String> userTypeBox;
    private final JButton signUpButton;

    public SignUpFrame() {
        setTitle("Shefaa Hospital - Sign Up");
        setSize(800, 600); // Standard window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Use BackgroundPanel for the background with gradient effect
        BackgroundPanel bgPanel = new BackgroundPanel();
        bgPanel.setLayout(new GridBagLayout()); // Use GridBagLayout for better control over positioning
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding for components

        // Add logo to the frame
        ImageIcon logoIcon = new ImageIcon("logo.png");  // Replace with the correct path to your logo image
        Image logoImage = logoIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);  // Resize the logo if needed
        JLabel logoLabel = new JLabel(new ImageIcon(logoImage));
        gbc.gridwidth = 2; // Title spans 2 columns
        gbc.gridx = 0;
        gbc.gridy = 0;
        bgPanel.add(logoLabel, gbc);

        // Title Label
        JLabel titleLabel = new JLabel("Sign Up for Shefaa Hospital");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(50, 150, 250)); // Light blue color
        gbc.gridwidth = 2; // Title spans 2 columns
        gbc.gridx = 0;
        gbc.gridy = 1;
        bgPanel.add(titleLabel, gbc);

        // Username field
        JLabel userLabel = new JLabel("Username:");
        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(250, 30)); // Set a preferred size for the text field
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        bgPanel.add(userLabel, gbc);
        gbc.gridx = 1;
        bgPanel.add(usernameField, gbc);

        // Password field
        JLabel passLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(250, 30)); // Set a preferred size for the text field
        gbc.gridx = 0;
        gbc.gridy = 3;
        bgPanel.add(passLabel, gbc);
        gbc.gridx = 1;
        bgPanel.add(passwordField, gbc);

        // Phone Number field
        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneField = new JTextField();
        phoneField.setPreferredSize(new Dimension(250, 30)); // Set a preferred size for the text field
        gbc.gridx = 0;
        gbc.gridy = 4;
        bgPanel.add(phoneLabel, gbc);
        gbc.gridx = 1;
        bgPanel.add(phoneField, gbc);

        // Address field
        JLabel addressLabel = new JLabel("Address:");
        addressField = new JTextField();
        addressField.setPreferredSize(new Dimension(250, 30)); // Set a preferred size for the text field
        gbc.gridx = 0;
        gbc.gridy = 5;
        bgPanel.add(addressLabel, gbc);
        gbc.gridx = 1;
        bgPanel.add(addressField, gbc);

        // Gender selection
        JLabel genderLabel = new JLabel("Gender:");
        genderComboBox = new JComboBox<>(new String[]{"Male", "Female"});
        gbc.gridx = 0;
        gbc.gridy = 6;
        bgPanel.add(genderLabel, gbc);
        gbc.gridx = 1;
        bgPanel.add(genderComboBox, gbc);

        // Age field
        JLabel ageLabel = new JLabel("Age:");
        ageField = new JTextField();
        ageField.setPreferredSize(new Dimension(250, 30)); // Set a preferred size for the text field
        gbc.gridx = 0;
        gbc.gridy = 7;
        bgPanel.add(ageLabel, gbc);
        gbc.gridx = 1;
        bgPanel.add(ageField, gbc);

        // User Type selection (Only "Patient")
        JLabel typeLabel = new JLabel("User Type:");
        userTypeBox = new JComboBox<>(new String[]{"Patient"});
        gbc.gridx = 0;
        gbc.gridy = 8;
        bgPanel.add(typeLabel, gbc);
        gbc.gridx = 1;
        bgPanel.add(userTypeBox, gbc);

        // Sign Up Button
        signUpButton = new JButton("Sign Up");
        signUpButton.setBackground(new Color(50, 150, 250)); // Light blue color for buttons
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setFont(new Font("Arial", Font.BOLD, 16));
        signUpButton.setFocusPainted(false);
        signUpButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signUpButton.setPreferredSize(new Dimension(200, 40));
        gbc.gridwidth = 2; // Button spans 2 columns
        gbc.gridx = 0;
        gbc.gridy = 9;
        bgPanel.add(signUpButton, gbc);
// Sign Up Button Action
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String phoneNumber = phoneField.getText();
                String address = addressField.getText();
                String gender = (String) genderComboBox.getSelectedItem();
                int age = Integer.parseInt(ageField.getText()); // Assuming age is an integer

                String userType = userTypeBox.getSelectedItem().toString().toLowerCase();

                // Ensure only patients can sign up
                if (userType.equals("patient")) {
                    try {
                        // Call RegisterService to save patient details
                        if (!RegisterService.registerPatient(username, password, phoneNumber, address, gender, age)) {
                            JOptionPane.showMessageDialog(null, "❌ User already exists", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "✅ Sign Up Successful!");
                            dispose(); // Close sign-up frame after successful registration
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(SignUpFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "❌ Only patients can sign up.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }


        });
        // Set the background panel as the content pane
        setContentPane(bgPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SignUpFrame().setVisible(true));
    }
}
