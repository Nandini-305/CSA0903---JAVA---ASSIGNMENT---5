# Task B4 – Final Engineering Decision

## B4.1 Migration
`PassFileMigration.java` reads the source file, validates field count and numeric values, reports rejected line numbers, and sends valid rows through the transactional batch loader from Task B3. The sample file contains valid and invalid records to demonstrate the rejection report. For a production CSV import, use the same quote-aware CSV parser as Task B1 rather than simple comma splitting.

## B4.2 Database metadata
The program prints database product, JDBC driver version and table names using `DatabaseMetaData`. This helps detect differences in database products, drivers and deployed schemas when moving the application to another server.

## B4.3 Production decision
Use a shared relational database for the 12-campus, 100-times-larger system. It supports concurrent counters, indexed pass-ID/route searches, transactions, constraints, centralized backups and access control. Text files are useful for export, interchange and small single-user stores. Java serialization is not suitable as the authoritative multi-user store because it is Java-specific and does not provide database transactions or concurrent multi-campus updates.

## Alternative: one serialized file per campus
Each campus can read its local file quickly, but a global search must inspect all 12 files (O(total records)); maintaining consistent updates, duplicate prevention and cross-campus reporting becomes harder. Space is approximately the sum of all campus files plus serialization overhead. Concurrent counters need file locks or a single-writer service per campus; coordination across campuses remains difficult. A shared database with indexes and transactions is the stronger production design.
