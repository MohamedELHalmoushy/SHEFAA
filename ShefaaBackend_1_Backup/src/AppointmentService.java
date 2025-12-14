/*import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class AppointmentService {

    public static void viewAppointments(Scanner sc) {
        System.out.println("📅 View All Appointments (Admin)");
    }

public static void viewDoctorAppointments(Scanner sc) {
    System.out.print("Enter your ID: ");
    
    int doctorId;
    try {
        doctorId = sc.nextInt(); // Ensure valid integer input
        sc.nextLine(); // Consume the newline character
    } catch (InputMismatchException e) {
        System.out.println("❌ Invalid input. Please enter a numeric ID.");
        sc.nextLine(); // Clear invalid input
        return;
    }

    String sql = "SELECT appointment_id, patient_id, appointment_date, appointment_time FROM Appointments WHERE doctor_id = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, doctorId);
        ResultSet rs = ps.executeQuery();

        boolean found = false;
        System.out.println("📅 Doctor Appointments:");

        while (rs.next()) {
            found = true;
            
            int appointmentId = rs.getInt("appointment_id");
            int patientId = rs.getInt("patient_id");
            String date = rs.getString("appointment_date");
            String time = rs.getString("appointment_time");

            System.out.println("Appointment ID: " + appointmentId +
                               ", Patient ID: " + patientId +
                               ", Date: " + date +
                               ", Time: " + time);
        }

        if (!found) {
            System.out.println("⚠️ No appointments found for Doctor ID: " + doctorId);
        }

    } catch (SQLException e) {
        System.out.println("❌ Error fetching appointments: " + e.getMessage());
    }
}

public static int getDoctorIdByName(String name) {
    String sql = "SELECT doctor_id FROM Doctors WHERE name = ?";
    
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt("doctor_id"); // ✅ Return the correct ID
        }

    } catch (Exception e) {
        System.out.println("❌ Error fetching doctor ID: " + e.getMessage());
    }
    
    return -1; // ❌ Return -1 if doctor not found
}



    public static void viewPatientAppointments(Scanner sc) {
        System.out.println("📅 View Patient Appointments");
    }
}
*/
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.*;
import java.sql.*;
public class AppointmentService {

    // ✅ View all appointments (Admin use)
    public static void viewAppointments() {
        JOptionPane.showMessageDialog(null, "📅 View All Appointments (Admin)");
    }
    public static void viewDoctorAppointments() {
        String doctorName = JOptionPane.showInputDialog("Enter your name to view your appointments:");
        if (doctorName == null) return;

        int doctorId = getDoctorIdByName(doctorName); // Get doctor_id by name
        if (doctorId == -1) {
            JOptionPane.showMessageDialog(null, "❌ Doctor not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT a.appointment_date, a.appointment_time, p.username as patient_name " +
                     "FROM Appointments a " +
                     "JOIN Patients p ON a.patient_id = p.patient_id " + // Join to get patient name
                     "WHERE a.doctor_id = ?"; // Use doctor_id
        StringBuilder appointments = new StringBuilder("📅 Appointments:\n");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, doctorId); // Use doctorId
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                appointments.append("Patient: ").append(rs.getString("patient_name")) // Use patient_name from join
                            .append(", Date: ").append(rs.getDate("appointment_date"))
                            .append(", Time: ").append(rs.getTime("appointment_time"))
                            .append("\n");
            }

            JOptionPane.showMessageDialog(null, appointments.toString(), "Appointments", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "❌ Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ✅ Get doctor ID by name (You already have this, but I'm including it for completeness)
    public static int getDoctorIdByName(String name) {
        String sql = "SELECT doctor_id FROM Doctors WHERE username = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("doctor_id");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "❌ Error fetching doctor ID: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        return -1; // ❌ Return -1 if doctor not found
    }
    // ✅ View patient’s appointments (for GUI)
    public static void viewPatientAppointments() {
        JOptionPane.showMessageDialog(null, "📅 View Patient Appointments");
    }
}
