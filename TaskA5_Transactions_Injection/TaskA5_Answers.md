# Task A5 – Design for Correctness and Safety

## A5.1 JDBC transaction pseudocode
```text
SET autoCommit TO false
TRY
    UPDATE wallet SET balance = balance - 500 WHERE wallet_id = 'HOSTEL'
    IF affected rows != 1 OR balance would become negative THEN RAISE ERROR
    UPDATE wallet SET balance = balance + 500 WHERE wallet_id = 'CANTEEN'
    IF affected rows != 1 THEN RAISE ERROR
    COMMIT
CATCH database error
    ROLLBACK
    REPORT transfer failed
FINALLY
    RESTORE original autoCommit setting
END
```
Both updates commit together or neither does. Add sufficient-balance and row-count checks.

## A5.2 SQL injection
Concatenating input into SQL allows special characters to alter the query structure. For example, an ID such as `' OR '1'='1' -- ` may make the WHERE condition true and comment out the remaining password condition, depending on the exact SQL/dialect. This is why untrusted input must not be concatenated into SQL.

```java
String sql = "SELECT 1 FROM users WHERE id = ? AND pwd_hash = ?";
try (PreparedStatement ps = con.prepareStatement(sql)) {
    ps.setString(1, id);
    ps.setString(2, passwordHash);
    try (ResultSet rs = ps.executeQuery()) {
        boolean authenticated = rs.next();
    }
}
```
In production, compare a securely salted password hash using a password-hashing library; do not store plaintext passwords.

## Alternative: auto-commit
With two independent auto-commit updates, the debit may become permanent before a failure prevents the credit, losing ₹500. A transaction may add a small commit/coordination cost but protects atomic correctness. The dominant database work is still two updates; correctness is more important than a tiny timing difference.
