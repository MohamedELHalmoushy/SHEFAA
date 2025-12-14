import java.sql.*;

public class RegisterService {
    public static boolean registerPatient(String username, String password, String phoneNumber, String address, String gender, int age) throws SQLException {
    String sql = "INSERT INTO patients (username, password, phone_number, address, gender, age) VALUES (?, ?, ?, ?, ?, ?)";
    try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
        
        // Set the values for the placeholders
        ps.setString(1, username);
        ps.setString(2, password); // Ideally, password should be hashed
        ps.setString(3, phoneNumber);
        ps.setString(4, address);
        ps.setString(5, gender);
        ps.setInt(6, age);

        // Execute the query
        int rowsAffected = ps.executeUpdate();

        // Return true if the insertion was successful
        return rowsAffected > 0;

    } catch (SQLException e) {
        e.printStackTrace();  // Log the error
        return false;
    }
}

}
