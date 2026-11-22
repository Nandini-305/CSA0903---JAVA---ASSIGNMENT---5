import java.sql.*;

public class UnsafeAndSafeLogin {
    static boolean unsafeLogin(Connection con, String id, String pwd) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE id='" + id + "' AND pwd='" + pwd + "'";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            return rs.next();
        }
    }
    static boolean safeLogin(Connection con, String id, String pwd) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE id=? AND pwd=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, pwd);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }
    public static void main(String[] args) throws Exception {
        String url = System.getenv().getOrDefault("DB_URL",
                "jdbc:mysql://localhost:3306/java_assignment5");
        String user = System.getenv().getOrDefault("DB_USER", "root");
        String pass = System.getenv().getOrDefault("DB_PASSWORD", "YOUR_PASSWORD");
        try (Connection con = DriverManager.getConnection(url, user, pass)) {
            String attackId = "' OR '1'='1' -- ";
            System.out.println("Unsafe accepts crafted input: " + unsafeLogin(con, attackId, "wrong"));
            System.out.println("Safe accepts crafted input: " + safeLogin(con, attackId, "wrong"));
        }
    }
}
