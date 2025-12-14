import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DoctorLoginFrame extends JFrame {
    private JComboBox<String> patientComboBox;
    private final JButton viewMedicalHistoryButton;
    private final JButton addPatientButton;

    public DoctorLoginFrame() {
        setTitle("Doctor Dashboard - Shefaa Hospital");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        BackgroundPanel bgPanel = new BackgroundPanel(); // بدون صورة شعار، لكن ممكن نضيف لوجو كـ JLabel
        bgPanel.setLayout(null);

        // Header
        JLabel headerLabel = new JLabel("Welcome, Doctor");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerLabel.setForeground(new Color(30, 60, 90));
        headerLabel.setBounds(30, 20, 400, 30);
        bgPanel.add(headerLabel);

        int x = 50, y = 70, width = 250, height = 35, gap = 15;

        JButton viewAppointmentsButton = createStyledButton("📅 View Appointments", x, y, width, height);
        JButton viewPatientsButton = createStyledButton("🧍‍♂️ View Patient Details", x, y += height + gap, width, height);

        JLabel selectPatientLabel = new JLabel("Select Patient:");
        selectPatientLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        selectPatientLabel.setBounds(x, y += height + gap, width, 25);
        bgPanel.add(selectPatientLabel);

        patientComboBox = new JComboBox<>();
        patientComboBox.setBounds(x, y += 30, width, 30);
        bgPanel.add(patientComboBox);
        loadPatientUsernames();

        viewMedicalHistoryButton = createStyledButton("📋 View Medical History", x, y += 40 + gap, width, height);
        JButton updateMedicalHistoryButton = createStyledButton("✏️ Update Medical History", x, y += height + gap, width, height);
        addPatientButton = createStyledButton("➕ Add Patient", x, y += height + gap, width, height);
        JButton logoutButton = createStyledButton("🚪 Logout", x, y += height + gap, width, height);

        // Chat bubble
        JButton chatBubbleButton = ChatBotUtils.createChatBubble();
        chatBubbleButton.setBounds(700, 450, 60, 60);
        bgPanel.add(chatBubbleButton);

        // Add buttons to panel
        bgPanel.add(viewAppointmentsButton);
        bgPanel.add(viewPatientsButton);
        bgPanel.add(viewMedicalHistoryButton);
        bgPanel.add(updateMedicalHistoryButton);
        bgPanel.add(addPatientButton);
        bgPanel.add(logoutButton);

        // Button Actions
        viewAppointmentsButton.addActionListener(e -> AppointmentService.viewDoctorAppointments());
        viewPatientsButton.addActionListener(e -> PatientService.viewPatients());

        viewMedicalHistoryButton.addActionListener(e -> {
            String selectedUsername = (String) patientComboBox.getSelectedItem();
            if (selectedUsername != null && !selectedUsername.isEmpty()) {
                String medicalHistory = PatientService.viewMedicalHistory(selectedUsername);
                if (medicalHistory != null && !medicalHistory.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "📜 Medical History for " + selectedUsername + ":\n" + medicalHistory,
                            "Medical History", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "⚠️ No medical history found for " + selectedUsername + ".", 
                            "Medical History", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "⚠️ Please select a patient!", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        updateMedicalHistoryButton.addActionListener(e -> {
            String selectedUsername = (String) patientComboBox.getSelectedItem();
            if (selectedUsername != null && !selectedUsername.isEmpty()) {
                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                JTextField diagnosisField = new JTextField(20);
                JTextField treatmentField = new JTextField(20);
                JTextField doctorNameField = new JTextField(20);

                panel.add(new JLabel("Enter Diagnosis:"));
                panel.add(diagnosisField);
                panel.add(new JLabel("Enter Treatment:"));
                panel.add(treatmentField);
                panel.add(new JLabel("Enter Doctor's Name:"));
                panel.add(doctorNameField);

                int option = JOptionPane.showConfirmDialog(this, panel, "Update Medical History", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    String diagnosis = diagnosisField.getText();
                    String treatment = treatmentField.getText();
                    String doctorName = doctorNameField.getText();

                    if (diagnosis.isEmpty() || treatment.isEmpty() || doctorName.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Warning", JOptionPane.WARNING_MESSAGE);
                    } else {
                        PatientService.updateMedicalHistory(selectedUsername, diagnosis, treatment, doctorName);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a patient!", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        addPatientButton.addActionListener(e -> PatientService.addPatient());

        logoutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Logging out...");
            new LoginFrame().setVisible(true);
            dispose();
        });

        setContentPane(bgPanel);
        setVisible(true);
    }

    private JButton createStyledButton(String text, int x, int y, int w, int h) {
        JButton button = new JButton(text);
        button.setBounds(x, y, w, h);
        button.setFocusPainted(false);
        button.setBackground(new Color(30, 144, 255));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void loadPatientUsernames() {
        List<String> usernames = PatientService.getAllPatientUsernames();
        for (String username : usernames) {
            patientComboBox.addItem(username);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DoctorLoginFrame::new);
    }
}
