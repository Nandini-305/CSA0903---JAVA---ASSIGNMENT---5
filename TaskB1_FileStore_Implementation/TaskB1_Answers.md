# Task B1 – Implementation and Instrumentation

## Completed TODO 1
`PassStore.find()` opens a `BufferedReader` in try-with-resources, parses each line, skips records with an incorrect field count or invalid numeric fields, and returns the matching record. The included code also supports quoted CSV fields.

## Test plan
| Test | Input/action | Expected behaviour |
|---|---|---|
| Normal find | Search P1003 | Returns its six fields |
| Missing ID | Search P9999 | Returns null |
| Damaged line | Include `P1002,DAMAGED LINE` | Skips line and continues |
| Route listing | List route 12 | Returns only valid route-12 records |

The supplied `PassStore` main method prints the P1003 record, `null` for P1002, and route count.

## Buffering and growth measurement
Run `BufferedLineCounter` against generated 1 MB, 10 MB and 50 MB files. Record both timings in the table below. Repeat each measurement at least three times and compare median values; run the buffered and unbuffered tests on the same file and machine.

| File size | Unbuffered time (ms) | Buffered time (ms) | Observation |
|---|---:|---:|---|
| 1 MB | Fill after run | Fill after run | |
| 10 MB | Fill after run | Fill after run | |
| 50 MB | Fill after run | Fill after run | |

For lookup growth, create 1,000, 10,000 and 100,000 records and time a lookup near the end of the file. A linear scan should grow approximately in proportion to record count (O(n)). Do not submit invented timing values; enter measured results from your computer.
