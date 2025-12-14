import javax.swing.*;
import java.awt.*;

public class AdminLoginFrame extends JFrame {

    private final JButton manageDoctorsButton;
    private final JButton managePatientsButton;
    private final JButton viewAppointmentsButton;
    private final JButton logoutButton;
    private final JButton chatBubbleButton;
    private JLabel logoLabel;

    public AdminLoginFrame() {
        setTitle("Admin Dashboard");
        setSize(800, 600); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BackgroundPanel bgPanel = new BackgroundPanel();
        bgPanel.setLayout(null);


        manageDoctorsButton = new JButton("Manage Doctors");
        manageDoctorsButton.setBounds(100, 50, 200, 40);
        manageDoctorsButton.setBackground(new Color(100, 150, 255));
        manageDoctorsButton.setForeground(Color.WHITE);

        managePatientsButton = new JButton("Manage Patients");
        managePatientsButton.setBounds(100, 110, 200, 40);
        managePatientsButton.setBackground(new Color(100, 150, 255));
        managePatientsButton.setForeground(Color.WHITE);

        viewAppointmentsButton = new JButton("View Appointments");
        viewAppointmentsButton.setBounds(100, 170, 200, 40);
        viewAppointmentsButton.setBackground(new Color(100, 150, 255));
        viewAppointmentsButton.setForeground(Color.WHITE);

        logoutButton = new JButton("Logout");
        logoutButton.setBounds(100, 230, 200, 40);
        logoutButton.setBackground(new Color(255, 85, 85)); 
        logoutButton.setForeground(Color.WHITE);


        manageDoctorsButton.addActionListener(e -> DoctorService.manageDoctors());
        managePatientsButton.addActionListener(e -> PatientService.managePatients());
        viewAppointmentsButton.addActionListener(e -> AppointmentService.viewDoctorAppointments());
        logoutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Logging out...");
            dispose();
            new LoginFrame();
        });

        // ✅ استخدام زر الدردشة من ChatBotUtils
        chatBubbleButton = ChatBotUtils.createChatBubble();
        chatBubbleButton.setBounds(600, 400, 50, 50); // تعديل حجم الزر ومكانه

        // إضافة الأزرار إلى الـ panel
        bgPanel.add(manageDoctorsButton);
        bgPanel.add(managePatientsButton);
        bgPanel.add(viewAppointmentsButton);
        bgPanel.add(logoutButton);
        bgPanel.add(chatBubbleButton);

        setContentPane(bgPanel); 
        setLocationRelativeTo(null); 
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminLoginFrame::new);
    }
}
