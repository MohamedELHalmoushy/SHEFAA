import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=ShefaaDB;encrypt=false";
    private static final String USER = "sa"; // replace with your SQL Server login (sa)
    private static final String PASSWORD = "123456"; // replace with your SQL Server password

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Connected to ShefaaDB");
        } catch (SQLException e) {
            System.out.println("❌ Connection Failed: " + e.getMessage());
        }
        return conn;
    }

    public static void main(String[] args) {
        getConnection();
    }
}
