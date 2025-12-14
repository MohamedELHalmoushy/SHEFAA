import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class AppointmentGUI {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel model;
    private JScrollPane scrollPane;
    
    public AppointmentGUI(int doctorId) {
        frame = new JFrame("Doctor Appointments");
        frame.setSize(2000, 2000);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        
        model = new DefaultTableModel();
        model.addColumn("Appointment ID");
        model.addColumn("Patient ID");
        model.addColumn("Date");
        model.addColumn("Time");
        
        table = new JTable(model);
        scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);
        
        loadAppointments(doctorId);
        
        frame.setVisible(true);
    }
    
    private void loadAppointments(int doctorId) {
        String sql = "SELECT appointment_id, patient_id, appointment_date, appointment_time FROM Appointments WHERE doctor_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, doctorId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("appointment_id"),
                        rs.getInt("patient_id"),
                        rs.getString("appointment_date"),
                        rs.getString("appointment_time")
                });
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error fetching appointments: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        int doctorId = Integer.parseInt(JOptionPane.showInputDialog("Enter Doctor ID:"));
        new AppointmentGUI(doctorId);
    }
}
