# Task A3 – Discover the Data-Access Design Pattern

## A3.1 Common pattern
Repeated steps: obtain a connection, prepare SQL, bind values, execute, convert `ResultSet` rows into Java objects, handle SQL exceptions, and close resources. Differences: entity/table names, columns, key type, SQL statements, and row-to-object mapping.

## A3.2 Generic DAO
```java
public interface Dao<T, K> {
    void insert(T entity) throws SQLException;
    Optional<T> findById(K id) throws SQLException;
    List<T> findAll() throws SQLException;
    void update(T entity) throws SQLException;
    void delete(K id) throws SQLException;
}
```
`StudentDao implements Dao<Student, Integer>` and contains student-specific SQL and mapping. Use try-with-resources for `Connection`, `PreparedStatement`, and `ResultSet`.

## A3.3 Student mapping
| Java field | SQL column | Notes |
|---|---|---|
| `id` | `student_id` | Primary key |
| `registerNo` | `reg_no` | Unique |
| `name` | `name` | VARCHAR |
| `email` | `email` | VARCHAR |

A DAO centralizes SQL and mapping. If a column is renamed, the DAO mapping and SQL are updated in one place. If SQL is scattered across service/GUI classes, many locations may need changes. Both designs have essentially the same per-query database time and O(1) additional application memory per row operation; DAO improves maintainability, not the database's asymptotic query cost.
