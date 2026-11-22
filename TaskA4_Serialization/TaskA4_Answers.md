# Task A4 – What Should Be Serialized?

## A4.1 Persisted fields
Persist durable hostel data: room identifiers/capacity and resident name/register number (and other non-secret business data required by the application). Mark the live `Connection` reference `transient`; it represents an active external resource and is not serializable or reusable after restart. Passwords should not be serialized in plaintext; preferably store a salted password hash in a secure authentication store, not in the object snapshot. If the field remains in the class for runtime use, mark it transient.

Serializing `Hostel` traverses its reachable non-transient object graph: its rooms and each room's residents are serialized recursively. Every reachable non-transient object must itself be serializable. Shared references are represented so the graph's reference relationships can be restored.

## A4.2 Plan for change
When an old serialized file is read by a newer `Resident` class, Java's default field matching is used if the `serialVersionUID` is compatible. The new `phoneNumber` field receives its default value (null for String) if absent in the old stream. An explicit stable `serialVersionUID` helps compatible class evolution. If the UID differs, deserialization throws `InvalidClassException`. Add validation/default migration after reading.

## Alternative: CSV/JSON
Text formats are usually more readable and portable, but can be larger than binary serialization and require explicit parsing/mapping. Saving/loading n residents is O(n) time and O(n) output/input storage (streaming parsers can reduce auxiliary memory). Adding a field is often easier with optional properties and schema/version handling. Java serialization is convenient for Java object graphs but is Java-specific and can be fragile across class changes.
