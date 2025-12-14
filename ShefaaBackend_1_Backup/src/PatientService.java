import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date;
import java.awt.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.sql.*; // If you're using JDBC
public class PatientService {
private static String loggedInPatientUsername;  // Static variable to store logged-in patient's username

    // Method to set logged-in patient's username
    public static void setLoggedInPatientUsername(String username) {
        loggedInPatientUsername = username;
    }

    // Method to get logged-in patient's username
    public static String getLoggedInPatientUsername() {
        return loggedInPatientUsername;
    }

public static void patientLogin(Scanner sc) {
    System.out.println("🔐 Patient Login");
    System.out.print("Username: ");
    String username = sc.nextLine();
    System.out.print("Password: ");
    String password = sc.nextLine();

    if (LoginService.validateLogin(username, password, "patient")) {
        System.out.println("✅ Access Granted!");
        patientMenu(sc, username);  // Pass username
    } else {
        System.out.println("➡️ Access Denied.");
    }
}

        public static void managePatients() {
        String[] options = {"Add Patient", "View Patients", "Update Patient", "Delete Patient", "Cancel"};
        
        // Show the options dialog
        int choice = JOptionPane.showOptionDialog(null, "Choose an action:", 
                "Manage Patients", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, 
                null, options, options[0]);

        // Handle the user's choice
        switch (choice) {
            case 0:
                addPatient(); // Calls method to add a patient
                break;
            case 1:
                viewPatients(); // Calls method to view patients
                break;
            case 2:
                updatePatient(); // Calls method to update patient details
                break;
            case 3:
                deletePatient(); // Calls method to delete a patient
                break;
            default:
                System.out.println("❌ Operation cancelled.");
        }
    }
static void addPatient() {
    // Get patient information from the user via input dialogs
    String username = JOptionPane.showInputDialog("Enter Patient Name:");
    
    // Check if the username already exists (case-sensitive)
    if (isUsernameExists(username)) {
        JOptionPane.showMessageDialog(null, "❌ Invalid name. Username already exists. Please write another name.", "Error", JOptionPane.ERROR_MESSAGE);
        return;  // Exit the method if username exists
    }

    String password = JOptionPane.showInputDialog("Enter Patient Password:");
    String phone = JOptionPane.showInputDialog("Enter Patient Phone:");
    String address = JOptionPane.showInputDialog("Enter Patient Address:");
    String gender = JOptionPane.showInputDialog("Enter Patient Gender:");
    String ageStr = JOptionPane.showInputDialog("Enter Patient Age:");

    // Try to parse the age as an integer
    int age = 0;
    try {
        age = Integer.parseInt(ageStr);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Invalid age format.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    String sql = "INSERT INTO Patients (username, password, phone_number, address, gender, age) VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, username);
        ps.setString(2, password);
        ps.setString(3, phone);
        ps.setString(4, address);
        ps.setString(5, gender);
        ps.setInt(6, age);

        int rows = ps.executeUpdate();
        if (rows > 0) {
            JOptionPane.showMessageDialog(null, "✅ Patient added successfully!");
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "❌ Error adding patient: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

// This method checks if the username already exists (case-sensitive)
private static boolean isUsernameExists(String username) {
    String sql = "SELECT COUNT(*) FROM Patients WHERE BINARY username = ?"; // BINARY ensures case-sensitive comparison

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;  // If count > 0, username exists
        }
    } catch (Exception e) {
        System.out.println("❌ Error checking username: " + e.getMessage());
    }
    return false;  // Username doesn't exist
}


 static void viewPatients() {
    String sql = "SELECT * FROM Patients";
    StringBuilder patientsList = new StringBuilder();

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        boolean hasPatients = false;
        patientsList.append("👨‍⚕️ List of Patients:\n");
        while (rs.next()) {
            hasPatients = true;
            patientsList.append("ID: ").append(rs.getInt("patient_id"))
                        .append(", Name: ").append(rs.getString("username"))
                        .append(", Phone: ").append(rs.getString("phone_number"))
                        .append(", Address: ").append(rs.getString("address"))
                        .append(", Gender: ").append(rs.getString("gender"))
                        .append(", Age: ").append(rs.getInt("age"))
                        .append("\n");
        }

        if (!hasPatients) {
            JOptionPane.showMessageDialog(null, "No patients found.", "No Data", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, patientsList.toString(), "Patients List", JOptionPane.INFORMATION_MESSAGE);
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "❌ Error fetching patients: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

static void updatePatient() {
    String patientIdStr = JOptionPane.showInputDialog("Enter Patient ID to update:");
    int patientId = 0;

    try {
        patientId = Integer.parseInt(patientIdStr);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "❌ Invalid Patient ID.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Check if the patient exists before updating
    if (!doesPatientExist(patientId)) {
        JOptionPane.showMessageDialog(null, "⚠️ Patient not found.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    String newPhone = JOptionPane.showInputDialog("Enter new Phone:");
    String newAddress = JOptionPane.showInputDialog("Enter new Address:");

    String sql = "UPDATE Patients SET phone_number = ?, address = ? WHERE patient_id = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, newPhone);
        ps.setString(2, newAddress);
        ps.setInt(3, patientId);

        int rows = ps.executeUpdate();
        if (rows > 0) {
            JOptionPane.showMessageDialog(null, "✅ Patient updated successfully!");
        } else {
            JOptionPane.showMessageDialog(null, "⚠️ Patient not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "❌ Error updating patient: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

// Helper method to check if the patient exists in the database
private static boolean doesPatientExist(int patientId) {
    String sql = "SELECT COUNT(*) FROM Patients WHERE patient_id = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, patientId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;  // If count > 0, patient exists
        }
    } catch (Exception e) {
        System.out.println("❌ Error checking patient existence: " + e.getMessage());
    }
    return false;  // Patient doesn't exist
}


public static void viewPersonalInfo(String username) {
    String sql = "SELECT * FROM Patients WHERE username = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            System.out.println("\n🆔 Your Profile Information:");
            System.out.println("🔹 Name: " + rs.getString("username"));
            System.out.println("📞 Phone: " + rs.getString("phone_number"));
            System.out.println("🏠 Address: " + rs.getString("address"));
            System.out.println("⚧ Gender: " + rs.getString("gender"));
            System.out.println("🎂 Age: " + rs.getInt("age"));
        } else {
            System.out.println("⚠️ Error: Patient information not found.");
        }

    } catch (Exception e) {
        System.out.println("❌ Error fetching patient info: " + e.getMessage());
    }
}

static void deletePatient() {
    String patientIdStr = JOptionPane.showInputDialog("Enter Patient ID to delete:");
    int patientId = 0;

    try {
        patientId = Integer.parseInt(patientIdStr);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "❌ Invalid Patient ID.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Check if the patient exists before attempting to delete
    if (!doesPatientExist(patientId)) {  // Directly calling the existing method
        JOptionPane.showMessageDialog(null, "⚠️ Patient not found.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Confirm the deletion with the user
    int confirmation = JOptionPane.showConfirmDialog(null, 
        "Are you sure you want to delete this patient?", "Confirm Deletion", 
        JOptionPane.YES_NO_OPTION);

    if (confirmation == JOptionPane.NO_OPTION) {
        return;  // If the user cancels, exit the method
    }

    String sql = "DELETE FROM Patients WHERE patient_id = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, patientId);

        int rows = ps.executeUpdate();
        if (rows > 0) {
            JOptionPane.showMessageDialog(null, "✅ Patient deleted successfully!");
        } else {
            JOptionPane.showMessageDialog(null, "⚠️ Patient not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "❌ Error deleting patient: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
private static void patientMenu(Scanner sc, String username) {
    int choice;
    do {
        System.out.println("\n📋 Patient Dashboard");
        System.out.println("1. View Personal Information");
        System.out.println("2. Update Contact Details");
        System.out.println("3. View Appointments");
        System.out.println("4. Book an Appointment");  // ✅ New Option
        System.out.println("5. View Medical History");
        System.out.println("0. Logout");
        System.out.print("Enter choice: ");
        choice = sc.nextInt();
        sc.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                viewPersonalInfo(username);
                break;
            case 2:
                updatePatient();
                break;
            case 3:
                viewAppointments(username);
                break;
            case 4:
                bookAppointment(sc, username); // ✅ Call new function
                break;
            case 5:
                viewMedicalHistory(username);
                break;
            case 0:
                System.out.println("🔙 Logging out...");
                break;
            default:
                System.out.println("❌ Invalid choice, try again.");
        }
    } while (choice != 0);
}

public static void updateMedicalHistory(String username, String diagnosis, String treatment, String doctorName) {
        String sql = "INSERT INTO MedicalHistory (patient_id, diagnosis, treatment, doctor_name, date) " +
             "SELECT patient_id, ?, ?, ?, GETDATE() FROM Patients WHERE username = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, diagnosis);
            ps.setString(2, treatment);
            ps.setString(3, doctorName);
            ps.setString(4, username);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "✅ Medical history updated successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "❌ Error: Patient not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "❌ Error updating medical history: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
static String viewMedicalHistory(String username) {
    String sql = "SELECT mh.diagnosis, mh.treatment, p.username, mh.date, mh.doctor_name " +
                 "FROM MedicalHistory mh " +
                 "JOIN Patients p ON mh.patient_id = p.patient_id " +
                 "WHERE p.username = ?";

    StringBuilder medicalHistory = new StringBuilder();
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();

        boolean hasRecords = false;
        
        // Collect medical history records
        while (rs.next()) {
            hasRecords = true;
            medicalHistory.append("📅 Date: ").append(rs.getDate("date")).append("\n")
                          .append("🩺 Doctor: ").append(rs.getString("doctor_name")).append("\n")
                          .append("🦠 Diagnosis: ").append(rs.getString("diagnosis")).append("\n")
                          .append("💊 Treatment: ").append(rs.getString("treatment")).append("\n")
                          .append("---------------------------\n");
        }

        if (!hasRecords) {
            return "📜 Your Medical History:\n⚠️ No medical history found.";
        }
        
        return "📜 Your Medical History:\n" + medicalHistory.toString();
        
    } catch (Exception e) {
        return "❌ Error fetching medical history: " + e.getMessage();
    }
    
}


private static void viewAppointments(String username) {
    String sql = "SELECT d.name AS doctor_name, a.appointment_date, a.appointment_time " +  
                 "FROM Appointments a " +
                 "JOIN Patients p ON a.patient_id = p.patient_id " +
                 "JOIN Doctors d ON a.doctor_id = d.doctor_id " +
                 "WHERE p.username = ? " +
                 "ORDER BY a.appointment_date ASC, a.appointment_time ASC"; 

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();

        System.out.println("\n📅 Your Appointments:");
        boolean hasRecords = false;

        while (rs.next()) {
            hasRecords = true;
            System.out.println("🩺 Doctor: " + rs.getString("doctor_name"));
            System.out.println("📆 Date: " + rs.getDate("appointment_date"));
            System.out.println("⏰ Time: " + rs.getTime("appointment_time"));
            System.out.println("---------------------------");
        }

        if (!hasRecords) {
            System.out.println("⚠️ No upcoming appointments found.");
        }

    } catch (Exception e) {
        System.out.println("❌ Error fetching appointments: " + e.getMessage());
    }
}
public static void bookAppointment(Scanner sc, String username) {
    // Step 1: Get the patient's ID using their username
    int patientId = getPatientIdByUsername(username);
    if (patientId == -1) {
        System.out.println("❌ Error: Patient not found.");
        return;
    }

    // Step 2: Display available doctors
    System.out.println("\n👨‍⚕️ Available Doctors:");
    viewDoctors();

    // Step 3: Ask the patient to select a doctor
    System.out.print("Enter Doctor ID to book an appointment with: ");
    int doctorId = sc.nextInt();
    sc.nextLine(); // Clear buffer

    // Step 4: Ask for the appointment date and time
    System.out.print("Enter Appointment Date (YYYY-MM-DD): ");
    String appointmentDate = sc.nextLine();
    System.out.print("Enter Appointment Time (HH:MM:SS): ");
    String appointmentTime = sc.nextLine();

    // Step 5: Check if the slot is available
    if (isAppointmentAvailable(doctorId, appointmentDate, appointmentTime)) {
        // Step 6: Insert the appointment
        String sql = "INSERT INTO Appointments (patient_id, doctor_id, appointment_date, appointment_time) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, patientId);
            ps.setInt(2, doctorId);
            ps.setString(3, appointmentDate);
            ps.setString(4, appointmentTime);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Appointment booked successfully!");
            }

        } catch (Exception e) {
            System.out.println("❌ Error booking appointment: " + e.getMessage());
        }
    } else {
        System.out.println("⚠️ Selected doctor is not available at this time. Please choose another time.");
    }
}
private static int getPatientIdByUsername(String username) {
    String sql = "SELECT patient_id FROM Patients WHERE username = ?";
    
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt("patient_id");
        }

    } catch (Exception e) {
        System.out.println("❌ Error fetching patient ID: " + e.getMessage());
    }
    return -1; // Return -1 if patient not found
}
private static boolean isAppointmentAvailable(int doctorId, String appointmentDate, String appointmentTime) {
    String sql = "SELECT 1 FROM Appointments WHERE doctor_id = ? AND appointment_date = ? AND appointment_time = ?";
    
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setInt(1, doctorId);
        ps.setString(2, appointmentDate);
        ps.setString(3, appointmentTime);
        ResultSet rs = ps.executeQuery();

        return !rs.next(); // If no existing appointment found, the slot is available

    } catch (Exception e) {
        System.out.println("❌ Error checking appointment availability: " + e.getMessage());
    }
    return false;
}
private static void viewDoctors() {
    String sql = "SELECT doctor_id, name, specialization FROM Doctors";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            System.out.println("ID: " + rs.getInt("doctor_id") +
                               ", Name: " + rs.getString("name") +
                               ", Specialization: " + rs.getString("specialization"));
        }

    } catch (Exception e) {
        System.out.println("❌ Error fetching doctors: " + e.getMessage());
    }
}
    public static ArrayList getAllPatientUsernames() {
    ArrayList usernames = new ArrayList();
    String sql = "SELECT username FROM Patients";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            usernames.add(rs.getString("username"));
        }

    } catch (Exception e) {
        System.out.println("❌ Error loading usernames: " + e.getMessage());
    }

    return usernames;
}
}

