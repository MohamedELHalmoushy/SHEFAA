import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.*;
import java.awt.*;
import javax.swing.JOptionPane;
import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PatientDashboard {
    final String username;
    private JFrame frame;
    private JPanel panel;
    private final JLabel welcomeLabel;
    private final JButton viewInfoButton;
    private final JButton updateInfoButton;
    private final JButton viewAppointmentsButton;
    private final JButton bookAppointmentButton;
    private JButton logoutButton;
    private final JButton viewMedicalHistoryButton;

    public PatientDashboard(String username) {
        this.username = username;

        // Initialize frame and background panel
        frame = new JFrame("Patient Dashboard");

        // BackgroundPanel with gradient
        BackgroundPanel bgPanel = new BackgroundPanel();
        bgPanel.setLayout(null); // Use null layout to manually position components

        // Add the chat bubble button to open chat bot
        JButton chatBubbleButton = ChatBotUtils.createChatBubble(); 
        chatBubbleButton.setBounds(10, 10, 50, 50); // Set position of the button
        bgPanel.add(chatBubbleButton); 

        // Initialize components
        welcomeLabel = new JLabel("Welcome, " + username + "!", SwingConstants.CENTER);
        viewInfoButton = new JButton("View Personal Information");
        updateInfoButton = new JButton("Update Contact Details");
        viewAppointmentsButton = new JButton("View Appointments");
        bookAppointmentButton = new JButton("Book an Appointment");
        
        // Initialize viewMedicalHistoryButton and set its bounds
        viewMedicalHistoryButton = new JButton("View Medical History");
        viewMedicalHistoryButton.setBounds(100, 200, 350, 40);
        
        logoutButton = new JButton("Logout");

        // Set bounds for each component
        welcomeLabel.setBounds(100, 50, 350, 30);
        viewInfoButton.setBounds(100, 100, 350, 30);
        updateInfoButton.setBounds(100, 150, 350, 30);
        viewAppointmentsButton.setBounds(100, 250, 350, 30);
        bookAppointmentButton.setBounds(100, 300, 350, 30);
        logoutButton = new JButton("Logout");
        logoutButton.setBounds(100, 350, 350, 30);
        logoutButton.setBackground(new Color(255, 85, 85)); // لون logout باللون الأحمر
        logoutButton.setForeground(Color.WHITE);
        // Customize buttons for better visual appearance
        customizeButton(viewInfoButton);
        customizeButton(updateInfoButton);
        customizeButton(viewAppointmentsButton);
        customizeButton(bookAppointmentButton);
        customizeButton(viewMedicalHistoryButton);
        customizeButton(logoutButton);

        // Add components to the background panel
        bgPanel.add(welcomeLabel);
        bgPanel.add(viewInfoButton);
        bgPanel.add(updateInfoButton);
        bgPanel.add(viewAppointmentsButton);
        bgPanel.add(bookAppointmentButton);
        bgPanel.add(logoutButton);
        bgPanel.add(viewMedicalHistoryButton);

        // Add background panel to frame
        frame.add(bgPanel);

        // Frame settings
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center the window

        // Action listeners for buttons
        viewInfoButton.addActionListener(e -> showPatientInfo());
        updateInfoButton.addActionListener(e -> updateContactDetails());
        viewAppointmentsButton.addActionListener(e -> viewAppointments());
        bookAppointmentButton.addActionListener(e -> bookAppointment());
        logoutButton.addActionListener(e -> {
            frame.dispose();
            new LoginFrame(); // Open the login frame
            JOptionPane.showMessageDialog(null, "Logged Out Successfully!");
        });
        
        // Action listener for viewMedicalHistoryButton
        viewMedicalHistoryButton.addActionListener(e -> {
            String medicalHistory = PatientService.viewMedicalHistory(username);
            JOptionPane.showMessageDialog(frame, medicalHistory, "Medical History", JOptionPane.INFORMATION_MESSAGE);
        });

        // Action listener for the chat bubble button
        chatBubbleButton.addActionListener(e -> openChatBot());
    }

    private void customizeButton(JButton button) {
        button.setBackground(new Color(50, 150, 250)); // Light blue color for buttons
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(200, 40));
    }

    private void openChatBot() {
        // Show dialog with options for the user
        String[] options = {"1. Book an appointment", "2. View appointments", "3. Return to main menu"};
        String selectedOption = (String) JOptionPane.showInputDialog(
                frame,
                "Please select an option:",
                "Chat Bot Options",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (selectedOption != null) {
            switch (selectedOption) {
                case "1. Book an appointment":
                    bookAppointment();
                    break;
                case "2. View appointments":
                    viewAppointments();
                    break;
                case "3. Return to main menu":
                    frame.dispose(); // Close current window and return to Login screen
                    new LoginFrame(); // Open login frame again
                    break;
                default:
                    JOptionPane.showMessageDialog(frame, "Invalid option selected.");
                    break;
            }
        }
    }
    private void showPatientInfo() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM Patients WHERE username = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String info = "Name: " + rs.getString("username") + "\n" +
                              "Age: " + rs.getInt("age") + "\n" +
                              "Address: " + rs.getString("address") + "\n" +
                              "Phone: " + rs.getString("phone_number");

                JOptionPane.showMessageDialog(frame, info, "Patient Information", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "❌ No information found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "❌ Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateContactDetails() {
        String newPhone = JOptionPane.showInputDialog(frame, "Enter new phone number:");
        if (newPhone == null || newPhone.trim().isEmpty()) return;  // Cancel if empty

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE Patients SET phone_number = ? WHERE username = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, newPhone);
            ps.setString(2, username);

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(frame, "✅ Contact details updated!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "❌ Update failed!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "❌ Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    void viewAppointments() {
        try (Connection conn = DBConnection.getConnection()) {
            // First, get the patient_id from the username
            String getPatientIdSql = "SELECT patient_id FROM Patients WHERE username = ?";
            PreparedStatement ps1 = conn.prepareStatement(getPatientIdSql);
            ps1.setString(1, username);
            ResultSet rs1 = ps1.executeQuery();

            if (!rs1.next()) {
                JOptionPane.showMessageDialog(frame, "❌ No patient found with this username!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int patientId = rs1.getInt("patient_id");

            // Fetch appointments using patient_id
            String sql = "SELECT Doctors.username AS doctor_name, Appointments.appointment_date, Appointments.appointment_time " +
                         "FROM Appointments " +
                         "JOIN Doctors ON Appointments.doctor_id = Doctors.doctor_id " +
                         "WHERE Appointments.patient_id = ?";

            PreparedStatement ps2 = conn.prepareStatement(sql);
            ps2.setInt(1, patientId);
            ResultSet rs2 = ps2.executeQuery();

            StringBuilder appointments = new StringBuilder("Your Appointments:\n");

            while (rs2.next()) {
                appointments.append("Doctor: ").append(rs2.getString("doctor_name"))
                            .append(" | Date: ").append(rs2.getString("appointment_date"))
                            .append(" | Time: ").append(rs2.getString("appointment_time"))
                            .append("\n");
            }

            JOptionPane.showMessageDialog(frame, appointments.toString(), "Appointments", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "❌ Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

void bookAppointment() {
    try (Connection conn = DBConnection.getConnection()) {
        // 1. Get patient ID
        String getPatientIdSql = "SELECT patient_id FROM Patients WHERE username = ?";
        PreparedStatement ps1 = conn.prepareStatement(getPatientIdSql);
        ps1.setString(1, username);
        ResultSet rs1 = ps1.executeQuery();

        if (!rs1.next()) {
            JOptionPane.showMessageDialog(frame, "❌ Patient not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int patientId = rs1.getInt("patient_id");

        // 2. Get available doctors
        String getDoctorsSql = "SELECT doctor_id, username FROM Doctors";
        PreparedStatement ps2 = conn.prepareStatement(getDoctorsSql);
        ResultSet rs2 = ps2.executeQuery();

        java.util.Map<String, Integer> doctorMap = new java.util.HashMap<>();
        DefaultComboBoxModel<String> doctorModel = new DefaultComboBoxModel<>();

        while (rs2.next()) {
            String docName = rs2.getString("username");
            int docId = rs2.getInt("doctor_id");
            doctorModel.addElement(docName);
            doctorMap.put(docName, docId);
        }

        JComboBox<String> doctorCombo = new JComboBox<>(doctorModel);

        // 3. ComboBox for time slots
        String[] timeSlots = {
            "09:00:00", "10:00:00", "11:00:00", "12:00:00",
            "13:00:00", "14:00:00", "15:00:00", "16:00:00",
            "17:00:00", "18:00:00", "19:00:00", "20:00:00",
            "21:00:00", "22:00:00", "23:00:00", "24:00:00"
        };
        JComboBox<String> timeCombo = new JComboBox<>(timeSlots);

        // 4. JDateChooser for selecting date (with minimum date set to today)
        com.toedter.calendar.JDateChooser dateChooser = new com.toedter.calendar.JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");
        dateChooser.setMinSelectableDate(new java.util.Date()); // Disable past dates

        // 5. Create panel
        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Choose Doctor:"));
        panel.add(doctorCombo);
        panel.add(new JLabel("Choose Time:"));
        panel.add(timeCombo);
        panel.add(new JLabel("Choose Date:"));
        panel.add(dateChooser);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Book Appointment", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) return;

        // 6. Retrieve user selections
        String selectedDoctor = (String) doctorCombo.getSelectedItem();
        int doctorId = doctorMap.get(selectedDoctor);
        String selectedTime = (String) timeCombo.getSelectedItem();
        Date selectedDate = dateChooser.getDate();

        if (selectedDate == null) {
            JOptionPane.showMessageDialog(frame, "❌ Please select a valid date.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Extra validation to ensure selected date is not before today
        Date today = new Date();
        if (selectedDate.before(today)) {
            JOptionPane.showMessageDialog(frame, "❌ You cannot book an appointment in the past!", "Invalid Date", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String appointmentDate = new SimpleDateFormat("yyyy-MM-dd").format(selectedDate);

        // 7. Check if the slot is available
        String checkSql = "SELECT * FROM Appointments WHERE doctor_id = ? AND appointment_date = ? AND appointment_time = ?";
        PreparedStatement checkStmt = conn.prepareStatement(checkSql);
        checkStmt.setInt(1, doctorId);
        checkStmt.setString(2, appointmentDate);
        checkStmt.setString(3, selectedTime);
        ResultSet rsCheck = checkStmt.executeQuery();

        if (rsCheck.next()) {
            JOptionPane.showMessageDialog(frame, "⚠️ This slot is already taken. Please choose another.", "Unavailable", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 8. Book the appointment
        String insertSql = "INSERT INTO Appointments (patient_id, doctor_id, appointment_date, appointment_time) VALUES (?, ?, ?, ?)";
        PreparedStatement ps3 = conn.prepareStatement(insertSql);
        ps3.setInt(1, patientId);
        ps3.setInt(2, doctorId);
        ps3.setString(3, appointmentDate);
        ps3.setString(4, selectedTime);

        int inserted = ps3.executeUpdate();
        if (inserted > 0) {
            JOptionPane.showMessageDialog(frame, "✅ Appointment booked successfully!");
        } else {
            JOptionPane.showMessageDialog(frame, "❌ Failed to book appointment.");
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(frame, "❌ Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}


public static void main(String[] args) {
        // Prompt user to enter a username or hard-code one
        String username = JOptionPane.showInputDialog("Enter your username:");

        if (username != null && !username.trim().isEmpty()) {
            // Create the PatientDashboard instance and pass the username
            PatientDashboard dashboard = new PatientDashboard(username);
            dashboard.setVisible(true); // Show the dashboard window
        } else {
            JOptionPane.showMessageDialog(null, "❌ Username cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
public void setVisible(boolean visible) {
    frame.setVisible(visible);
}
}
