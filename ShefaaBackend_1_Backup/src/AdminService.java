import java.util.Scanner;

public class AdminService {

    public static void adminLogin(Scanner sc) {
        System.out.println("🔐 Admin Login");
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();

        if (LoginService.validateLogin(username, password, "admin")) {
            adminMenu(sc);
        } else {
            System.out.println("➡️ Access Denied.");
        }
    }

    private static void adminMenu(Scanner sc) {
        int choice;
        do {
            System.out.println("\n📋 Admin Menu");
            System.out.println("1. Manage Doctors");
            System.out.println("2. Manage Patients");
            System.out.println("3. View Appointments");
            System.out.println("0. Logout");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    DoctorService.manageDoctors();
                    break;
                case 2:
                    PatientService.managePatients();
                    break;
                case 3:
                    AppointmentService.viewDoctorAppointments();
                    break;
                case 0:
                    System.out.println("🔒 Logged out.");
                    break;
                default:
                    System.out.println("❌ Invalid choice.");
            }

        } while (choice != 0);
    }
}

