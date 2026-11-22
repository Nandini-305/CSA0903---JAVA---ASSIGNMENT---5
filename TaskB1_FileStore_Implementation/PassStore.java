import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class PassStore {
    private final Path file;
    public PassStore(Path file) { this.file = file; }

    public void add(String passId, String regNo, String name, int route,
                    String validityDate, double amount) throws IOException {
        try (BufferedWriter w = Files.newBufferedWriter(file, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            w.write(String.join(",", csv(passId), csv(regNo), csv(name),
                    String.valueOf(route), csv(validityDate), String.valueOf(amount)));
            w.newLine();
        }
    }

    // CSV parser supporting quoted fields and doubled quote characters.
    private static String[] parseCsv(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean quoted = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (quoted && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    field.append('"'); i++;
                } else quoted = !quoted;
            } else if (c == ',' && !quoted) {
                fields.add(field.toString()); field.setLength(0);
            } else field.append(c);
        }
        if (quoted) throw new IllegalArgumentException("Unclosed quoted field");
        fields.add(field.toString());
        return fields.toArray(new String[0]);
    }
    private static String csv(String value) {
        if (value == null) throw new IllegalArgumentException("Null field");
        if (value.contains(",") || value.contains("\"") || value.contains("\n"))
            return "\"" + value.replace("\"", "\"\"") + "\"";
        return value;
    }

    /** Returns six fields or null; malformed lines are skipped. */
    public String[] find(String passId) throws IOException {
        try (BufferedReader r = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            while ((line = r.readLine()) != null) {
                try {
                    String[] f = parseCsv(line);
                    if (f.length != 6) continue;
                    Integer.parseInt(f[3]);
                    Double.parseDouble(f[5]);
                    if (f[0].equals(passId)) return f;
                } catch (IllegalArgumentException ex) {
                    // Damaged record: skip it and continue scanning.
                }
            }
        }
        return null;
    }

    public List<String[]> listByRoute(int route) throws IOException {
        List<String[]> result = new ArrayList<>();
        try (BufferedReader r = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            while ((line = r.readLine()) != null) {
                try {
                    String[] f = parseCsv(line);
                    if (f.length == 6 && Integer.parseInt(f[3]) == route
                            && Double.parseDouble(f[5]) >= 0) result.add(f);
                } catch (IllegalArgumentException ignored) { }
            }
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        Path f = Files.createTempFile("passes", ".csv");
        PassStore store = new PassStore(f);
        store.add("P1001", "192472277", "Neelam Nandini", 12, "2027-03-31", 4500);
        Files.writeString(f, "P1002,DAMAGED LINE\n", StandardOpenOption.APPEND);
        store.add("P1003", "192311003", "Divya", 7, "2027-03-31", 3800);
        System.out.println(Arrays.toString(store.find("P1003")));
        System.out.println(Arrays.toString(store.find("P1002")));
        System.out.println("Route 12 count: " + store.listByRoute(12).size());
        Files.deleteIfExists(f);
    }
}
