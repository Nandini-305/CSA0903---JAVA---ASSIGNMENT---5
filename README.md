
# CSA09 – Java Assignment 5 (CO5)
**Student Name:** Neelam Nandini  
**Register Number:** 192472277  
**Topic:** Data Persistence Engineering – Files and Databases

## Folder guide
- `TaskA1_File_Format_Design` – delimited/binary layouts, file operations, recovery design, and fixed-record comparison.
- `TaskA2_Buffered_IO` – read-call analysis, buffered implementation, and whole-file alternative.
- `TaskA3_DAO_Pattern` – generic DAO design and Student object/table mapping.
- `TaskA4_Serialization` – transient fields, object graph, versioning, and CSV/JSON comparison.
- `TaskA5_Transactions_Injection` – transaction pseudocode and safe PreparedStatement login.
- `TaskB1_FileStore_Implementation` – completed PassStore TODO, line counter, tests, and benchmark program.
- `TaskB2_Adversarial_Inputs` – unsafe/safe login demonstration and damaged-file tests.
- `TaskB3_JDBC_Batch` – completed PassLoader TODOs, SQL schema, and JDBC benchmark/transaction program.
- `TaskB4_Final_Decision` – migration utility, DatabaseMetaData inspection, and production decision.

## Requirements
- JDK 17 or later (JDK 11+ is sufficient for most examples).
- MySQL 8.x for Tasks B2–B4.
- MySQL Connector/J JAR for JDBC tasks.

## Compile and run
Open a terminal in the relevant task folder:
```bash
javac FileName.java
java ClassName
```
For JDBC programs, add Connector/J to the classpath and configure the database credentials in the source or environment variables. Windows example:
```powershell
javac -cp ".;mysql-connector-j-8.x.x.jar" *.java
java -cp ".;mysql-connector-j-8.x.x.jar" ClassName
```
Linux/macOS use `:` instead of `;` in the classpath.

## Important execution notes
1. Run each task yourself and save actual screenshots in that task's `output-screenshots` folder.
2. JDBC tasks require a locally created MySQL database. Run each task's `schema.sql` first.
3. Replace the database password placeholder in the Java files, or set `DB_PASSWORD` as an environment variable.
4. Record benchmark timings from your own computer. Timing values are machine-dependent; this ZIP does not fabricate measured results.
5. Create the required GitHub repository named `CSA09-192472277-Assignment5`, commit each task progressively, and add the faculty as collaborator if the repository is private.

## Student result
The package contains the design answers and source implementations for all A1–A5 and B1–B4 tasks. Database-backed tasks are prepared for local execution and require MySQL/Connector-J configuration. Screenshots and real benchmark measurements must be added after execution.
