import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginService {

    public static boolean validateLogin(String username, String password, String userType) {
        String table = "";
        switch (userType.toLowerCase()) {
            case "admin": table = "Admins"; break;
            case "doctor": table = "Doctors"; break;
            case "patient": table = "Patients"; break;
            default: return false;
        }

        String sql = "SELECT * FROM " + table + " WHERE username = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String dbUsername = rs.getString("username");
                String dbPassword = rs.getString("password");

                // Case-sensitive check
                if (username.equals(dbUsername) && password.equals(dbPassword)) {
                    System.out.println("✅ Login Successful! Welcome, " + username);
                    return true;
                } else {
                    System.out.println("❌ Invalid Credentials (Case mismatch)");
                    return false;
                }
            } else {
                System.out.println("❌ Invalid Credentials (User not found)");
                return false;
            }

        } catch (Exception e) {
            System.out.println("❌ Error during login: " + e.getMessage());
            return false;
        }
    }
}
