import java.io.*;
import java.nio.file.*;

public class BufferedLineCounter {
    public static long countUnbuffered(Path path) throws IOException {
        long lines = 0;
        try (InputStream in = new FileInputStream(path.toFile())) {
            int b;
            while ((b = in.read()) != -1) if (b == '\n') lines++;
        }
        return lines;
    }
    public static long countBuffered(Path path) throws IOException {
        long lines = 0;
        byte[] buffer = new byte[64 * 1024];
        try (InputStream in = new BufferedInputStream(Files.newInputStream(path), buffer.length)) {
            int n;
            while ((n = in.read(buffer)) != -1)
                for (int i = 0; i < n; i++) if (buffer[i] == '\n') lines++;
        }
        return lines;
    }
    public static void main(String[] args) throws Exception {
        Path p = Path.of(args.length == 0 ? "attendance.txt" : args[0]);
        long t1 = System.nanoTime(), a = countUnbuffered(p), t2 = System.nanoTime();
        long b = countBuffered(p), t3 = System.nanoTime();
        System.out.printf("Unbuffered lines=%d time=%.3f ms%n", a, (t2-t1)/1e6);
        System.out.printf("Buffered   lines=%d time=%.3f ms%n", b, (t3-t2)/1e6);
    }
}
