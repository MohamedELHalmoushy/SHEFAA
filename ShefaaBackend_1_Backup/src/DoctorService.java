import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DoctorService {

    public static void manageDoctors() {
        String[] options = {"Add Doctor", "View Doctors", "Update Doctor", "Delete Doctor", "Cancel"};
        int choice = JOptionPane.showOptionDialog(null, "Choose an action:", 
            "Manage Doctors", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, 
            null, options, options[0]);

        switch (choice) {
            case 0:
                addDoctor();
                break;
            case 1:
                viewDoctors();
                break;
            case 2:
                updateDoctor();
                break;
            case 3:
                deleteDoctor();
                break;
            default:
                System.out.println("❌ Operation cancelled.");
        }
    }

    public static void addDoctor() {
    String name = JOptionPane.showInputDialog("Enter Doctor Name:");
    if (name == null) return;

    String gender = JOptionPane.showInputDialog("Enter Gender:");
    String ageStr = JOptionPane.showInputDialog("Enter Age:");
    String degree = JOptionPane.showInputDialog("Enter Degree:");
    String salaryStr = JOptionPane.showInputDialog("Enter Salary:");
    String phoneNumber = JOptionPane.showInputDialog("Enter Phone Number:");
    String departmentIdStr = JOptionPane.showInputDialog("Enter Department ID:");
    String password = JOptionPane.showInputDialog("Enter Doctor Password:");
    String specialization = JOptionPane.showInputDialog("Enter Specialization:");

    if (gender == null || ageStr == null || salaryStr == null || departmentIdStr == null || password == null || specialization == null) return;

    int age, salary, departmentId;
    try {
        age = Integer.parseInt(ageStr);
        salary = Integer.parseInt(salaryStr);
        departmentId = Integer.parseInt(departmentIdStr);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Invalid input! Age, Salary, or Department ID must be numeric.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Check if the doctor already exists by username
    if (doesDoctorExistByUsername(name)) {
        JOptionPane.showMessageDialog(null, "❌ A doctor with this username already exists.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    String sql = "INSERT INTO Doctors (username, gender, age, degree, salary, phone_number, department_id, password, specialization) "
               + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, name);
        ps.setString(2, gender);
        ps.setInt(3, age);
        ps.setString(4, degree);
        ps.setInt(5, salary);
        ps.setString(6, phoneNumber);
        ps.setInt(7, departmentId);
        ps.setString(8, password);
        ps.setString(9, specialization);

        int rows = ps.executeUpdate();
        if (rows > 0) {
            JOptionPane.showMessageDialog(null, "✅ Doctor added successfully!");
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "❌ Error adding doctor: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}

private static boolean doesDoctorExistByUsername(String username) {
    String sql = "SELECT COUNT(*) FROM Doctors WHERE LOWER(username) = LOWER(?)";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, username);
        try (ResultSet rs = ps.executeQuery()) {
            return rs.next() && rs.getInt(1) > 0; // Returns true if a match is found
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "❌ Error checking doctor existence: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    return false;  // Doctor not found
}


    public static void viewDoctors() {
        String sql = "SELECT * FROM Doctors";
        StringBuilder doctorsList = new StringBuilder("📋 List of Doctors:\n");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                doctorsList.append("ID: ").append(rs.getInt("doctor_id"))
                           .append(", Name: ").append(rs.getString("username"))
                           .append(", Degree: ").append(rs.getString("degree"))
                           .append(", Phone: ").append(rs.getString("phone_number"))
                           .append("\n");
            }

            JOptionPane.showMessageDialog(null, doctorsList.toString(), "Doctors", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "❌ Error fetching doctors: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void updateDoctor() {
    String idStr = JOptionPane.showInputDialog("Enter Doctor ID to update:");
    if (idStr == null) return;

    int id;
    try {
        id = Integer.parseInt(idStr);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Invalid ID!", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Ensure that the doctor exists before proceeding
    if (!doesDoctorExist(id)) {
        JOptionPane.showMessageDialog(null, "⚠️ Doctor not found.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    String phoneNumber = JOptionPane.showInputDialog("Enter new Phone Number:");
    if (phoneNumber == null) return;

    String sql = "UPDATE Doctors SET phone_number = ? WHERE doctor_id = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, phoneNumber);
        ps.setInt(2, id);

        int rows = ps.executeUpdate();
        if (rows > 0) {
            JOptionPane.showMessageDialog(null, "✅ Doctor updated successfully!");
        } else {
            JOptionPane.showMessageDialog(null, "⚠️ Doctor not found.", "Warning", JOptionPane.WARNING_MESSAGE);
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "❌ Error updating doctor: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}

    public static void deleteDoctor() {
    String idStr = JOptionPane.showInputDialog("Enter Doctor ID to delete:");
    if (idStr == null) return;

    int id;
    try {
        id = Integer.parseInt(idStr);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Invalid ID!", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Ensure that the doctor exists before proceeding
    if (!doesDoctorExist(id)) {
        JOptionPane.showMessageDialog(null, "⚠️ Doctor not found.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    String sql = "DELETE FROM Doctors WHERE doctor_id = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, id);

        int rows = ps.executeUpdate();
        if (rows > 0) {
            JOptionPane.showMessageDialog(null, "✅ Doctor deleted successfully!");
        } else {
            JOptionPane.showMessageDialog(null, "⚠️ Doctor not found.", "Warning", JOptionPane.WARNING_MESSAGE);
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "❌ Error deleting doctor: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}
public static boolean validateDoctorLogin(String username, String password) {
    String sql = "SELECT * FROM Doctors WHERE (username) = LOWER(?) AND password = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, username);
        ps.setString(2, password);
        try (ResultSet rs = ps.executeQuery()) {
            return rs.next(); // ✅ Returns true if a match is found
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "❌ Database Error: " + e.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
        return false;
    }
}
private static boolean doesDoctorExist(int doctorId) {
    String sql = "SELECT COUNT(*) FROM Doctors WHERE doctor_id = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, doctorId);
        try (ResultSet rs = ps.executeQuery()) {
            return rs.next() && rs.getInt(1) > 0;
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "❌ Error checking doctor existence: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    return false;  // Doctor not found
}

}

