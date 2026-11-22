# Task A2 – Don’t Read What You Don’t Need

## A2.1 Predict the cost
A 50 MiB file contains 52,428,800 bytes. Calling `FileInputStream.read()` once per byte makes approximately 52.4 million Java read calls. Each call has method-call and stream overhead; depending on buffering inside the operating system/JVM, it may not mean one physical disk operation per byte, but the Java-level loop still incurs substantial overhead.

## A2.2 Buffered redesign
Use `BufferedInputStream` with a 64 KiB buffer, or read chunks into a byte array. Count byte value `'\n'`. The loop now makes roughly `fileSize / bufferSize` read calls (about 800 calls for 50 MiB with 64 KiB chunks), rather than about 52 million one-byte calls. Time complexity remains O(n); auxiliary memory is O(buffer size).

## A2.3 When buffering helps little
If the source is already a fast in-memory byte array, or the program performs only one small read, an extra buffer may add copying and setup overhead without meaningful benefit.

## Alternative: Files.readAllLines
`Files.readAllLines` has O(n) time but stores all decoded lines in memory, approximately O(file size) plus per-String/list overhead. A buffered stream uses O(buffer size) extra memory and is better for large files or streaming. `readAllLines` is convenient when the file is small enough to fit comfortably in memory and random access to lines is useful.
