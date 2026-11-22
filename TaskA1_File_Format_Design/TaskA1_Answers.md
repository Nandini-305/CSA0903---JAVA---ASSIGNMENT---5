# Task A1 – Design a File Format

## A1.1 Record layouts
**Delimited text (CSV-like):** `passId,regNo,name,routeNo,validityDate,amountPaid`
- UTF-8, one record per line.
- Escape a comma, quote, or newline inside a field by CSV quoting: wrap the field in double quotes and double each embedded quote.
- Example: `P1001,192472277,"Neelam, Nandini",12,2027-03-31,4500.00`.
- Validate the exact six-field count and field types when reading.

**Binary DataOutputStream layout:** write fields in this order:
`writeUTF(passId)`, `writeUTF(regNo)`, `writeUTF(name)`, `writeInt(routeNo)`, `writeUTF(validityDate as ISO-8601)`, `writeDouble(amountPaid)`.
The UTF fields are length-prefixed by `writeUTF`; the int is 4 bytes and double is 8 bytes. This is variable-length, self-describing at field boundaries, not a fixed-size record. A name containing commas needs no special handling in this format.

## A1.2 Operations – text-file pseudocode
**ADD PASS**
1. OPEN file using `BufferedWriter` with `Files.newBufferedWriter(..., CREATE, APPEND)`.
2. VALIDATE pass ID, register number, route, date and amount.
3. ENCODE all fields with CSV quoting.
4. WRITE encoded record and newline.
5. CLOSE writer automatically using try-with-resources.

**FIND BY PASS ID**
1. OPEN file using `Files.newBufferedReader` (`BufferedReader`).
2. WHILE a line is available, READ one line.
3. PARSE CSV line; IF field count is not six, RECORD damaged-line warning and CONTINUE.
4. IF pass ID equals requested ID, RETURN parsed fields.
5. RETURN NOT FOUND; CLOSE reader.

**LIST BY ROUTE**
1. OPEN file using `BufferedReader`.
2. WHILE a line is available, READ and parse it.
3. SKIP malformed records; IF route number equals requested route, DISPLAY record.
4. CLOSE reader.

`BufferedReader`/`BufferedWriter` reduce expensive small I/O operations and handle characters. `FileReader`/`FileWriter` are simpler character streams but use the platform default charset unless configured. `FileInputStream`/`FileOutputStream` are byte streams suited to raw bytes or binary formats; they are not the natural choice for CSV text.

## A1.3 Damaged data and recovery
- Damaged line: catch parse/number/date errors, log line number and reason, skip that record, continue processing. Never silently treat invalid data as a valid pass.
- Missing file: check `Files.exists`; for a new store create parent directories and an empty file; for a required existing store report a clear error rather than pretending it is empty.
- Incomplete write/power failure: write a complete new file to a temporary file, flush/close it, then atomically replace the old file where supported. For append-only use, add a checksum/record terminator and validate on startup; keep backups. File APIs alone cannot guarantee recovery from every hardware failure.

## Alternative: RandomAccessFile
With fixed-size records, record `i` is located at `i * recordSize`, so a known record index can be sought in O(1). But the task asks for lookup by pass ID, not index: without an index, searching IDs is still O(n). A separate ID-to-record-offset index can make lookup approximately O(1) average with a hash index, at extra storage and update complexity. Fixed-width name/register/date fields waste space when values are shorter than their allocated widths; long values need truncation or rejection. Sequential text scan is O(n), with compact variable-length storage.
