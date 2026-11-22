import java.sql.*;
import java.util.*;

public class PassLoaderDemo {
    public static void main(String[] args) throws Exception {
        String url = System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/java_assignment5");
        String user = System.getenv().getOrDefault("DB_USER", "root");
        String pass = System.getenv().getOrDefault("DB_PASSWORD", "YOUR_PASSWORD");
        int count = args.length == 0 ? 1000 : Integer.parseInt(args[0]);
        List<String[]> rows = new ArrayList<>();
        for (int i = 1; i <= count; i++)
            rows.add(new String[]{"P" + i, "REG" + i, "Student " + i, "12", "4500.00"});
        // Uncomment to test rollback: invalid numeric amount midway.
        // rows.set(count / 2, new String[]{"BAD", "REGX", "Invalid", "12", "not-a-number"});
        try (Connection con = DriverManager.getConnection(url, user, pass)) {
            long start = System.nanoTime();
            int inserted = PassLoader.loadAll(con, rows);
            System.out.printf("Inserted %d rows in %.3f ms%n", inserted, (System.nanoTime()-start)/1e6);
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM bus_pass")) {
                if (rs.next()) System.out.println("Rows currently in table: " + rs.getLong(1));
            }
            DatabaseMetaData md = con.getMetaData();
            System.out.println("Database: " + md.getDatabaseProductName());
            System.out.println("Driver: " + md.getDriverVersion());
        }
    }
}
