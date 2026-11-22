# Task B3 – Statement vs PreparedStatement vs Batch

The completed `PassLoader.loadAll()` binds all five values, adds each row to the batch, executes after each 1,000 rows, executes any remaining rows, commits once, and rolls back on SQL or runtime failure. The original auto-commit setting is restored.

## Experiment
Run the demo with `1000`, `10000`, and `100000`. Clear the table between independent trials. Implement and time the three required variants (Statement, PreparedStatement one row at a time, PreparedStatement batch) and repeat each trial. Use `ResultSetMetaData` to record column labels and types.

| Rows | Statement (ms) | PreparedStatement (ms) | Batch (ms) |
|---:|---:|---:|---:|
| 1,000 | Measure | Measure | Measure |
| 10,000 | Measure | Measure | Measure |
| 100,000 | Measure | Measure | Measure |

## Auto-commit vs transaction
With auto-commit on, already executed rows may remain after a later failure. With a single transaction, rollback removes all rows inserted by that transaction. Test by making one row invalid around the halfway point and verify row counts.

For a nightly import of 100,000 passes, use PreparedStatement batching inside a transaction, with validation and rejection logging. For a single counter issue, use one PreparedStatement execution (transaction if multiple related updates must be atomic). A multi-row INSERT or LOAD DATA may be faster for bulk ingestion but needs careful validation, error handling, and transaction design. Batch memory is approximately O(b) for batch size b; streaming batches bounds memory.
