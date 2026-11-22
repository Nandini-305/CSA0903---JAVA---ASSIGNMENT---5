# Task B2 – Engineer an Adversarial Input

Run `schema.sql`, configure Connector/J and DB credentials, then run `UnsafeAndSafeLogin`.

- Crafted ID: `' OR '1'='1' -- ` with an incorrect password.
- Unsafe concatenated SQL may accept it because the input changes the WHERE expression and comments out the password check (exact comment syntax can vary by SQL mode).
- PreparedStatement treats the entire input as a parameter value, so it searches for that literal ID and rejects it.

Damaged-file cases for `PassStore`: wrong field count, non-numeric route/amount, and empty line. The parser skips malformed lines and continues. A robust production importer should additionally write a line-numbered rejection report instead of silently skipping.

Escaping quotes or validating with a regular expressions is weaker than parameter binding: escaping can miss encoding/dialect edge cases, and validation cannot reliably distinguish all SQL syntax from data. PreparedStatement separates SQL code from values. Parameter binding overhead is normally small; both approaches still perform a database lookup whose cost depends mainly on indexes and database execution.
