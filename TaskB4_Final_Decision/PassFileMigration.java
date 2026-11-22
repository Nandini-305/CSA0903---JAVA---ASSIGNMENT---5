import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;

public class PassFileMigration {
    public static void main(String[] args) throws Exception {
        Path input = Path.of(args.length == 0 ? "passes.csv" : args[0]);
        String url = System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/java_assignment5");
        String user = System.getenv().getOrDefault("DB_USER", "root");
        String pass = System.getenv().getOrDefault("DB_PASSWORD", "YOUR_PASSWORD");
        List<String[]> valid = new ArrayList<>();
        List<String> rejected = new ArrayList<>();
        int lineNo = 0;
        try (BufferedReader br = Files.newBufferedReader(input)) {
            String line;
            while ((line = br.readLine()) != null) {
                lineNo++;
                String[] f = line.split(",", -1);
                try {
                    if (f.length != 5) throw new IllegalArgumentException("expected 5 fields");
                    Integer.parseInt(f[3]);
                    double amount = Double.parseDouble(f[4]);
                    if (amount < 0) throw new IllegalArgumentException("negative amount");
                    valid.add(f);
                } catch (RuntimeException ex) {
                    rejected.add("Line " + lineNo + ": " + ex.getMessage() + " | " + line);
                }
            }
        }
        try (Connection con = DriverManager.getConnection(url, user, pass)) {
            int loaded = PassLoader.loadAll(con, valid);
            System.out.println("Valid records loaded: " + loaded);
            System.out.println("Rejected records: " + rejected.size());
            for (String r : rejected) System.out.println(r);
            DatabaseMetaData md = con.getMetaData();
            System.out.println("Database product: " + md.getDatabaseProductName());
            System.out.println("Driver version: " + md.getDriverVersion());
            try (ResultSet tables = md.getTables(con.getCatalog(), null, "%", new String[]{"TABLE"})) {
                System.out.println("Tables:");
                while (tables.next()) System.out.println(" - " + tables.getString("TABLE_NAME"));
            }
        }
    }
}
