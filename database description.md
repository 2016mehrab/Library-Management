Here’s a description of the database schema based on the relationships shown in the diagram:

1. **Admin and StudentInfo**:
   - One Admin can create multiple `StudentInfo` records (1:N relationship).
   - Each `StudentInfo` record belongs to a single Admin.

2. **Student and StudentInfo**:
   - One Student will have exactly one `StudentInfo` record (1:1 relationship).
   - Each `StudentInfo` is associated with exactly one Student.

3. **Admin and LibrarianInfo**:
   - One Admin can create multiple `LibrarianInfo` records (1:N relationship).
   - Each `LibrarianInfo` belongs to a single Admin.

4. **Librarian and LibrarianInfo**:
   - One Librarian will have exactly one `LibrarianInfo` record (1:1 relationship).
   - Each `LibrarianInfo` is associated with exactly one Librarian.

5. **Student and BorrowRecord**:
   - One Student can have multiple `BorrowRecord` entries (1:N relationship).
   - Each `BorrowRecord` belongs to a single Student.

6. **Book and BorrowRecord**:
   - One Book can have multiple `BorrowRecord` entries (1:N relationship).
   - Each `BorrowRecord` references a single Book.

7. **BookRequest**:
   - Each `BookRequest` references one Student, one Book, and one Librarian (N:1 relationships for each).
   - A `BookRequest` can lead to one `BorrowRecord` (1:1 relationship).

8. **Book and BookCategory**:
   - One Book can belong to multiple categories via the `BookCategory` table (N:M relationship).
   - Each `BookCategory` entry references one Book and one Category.

9. **Author and Book**:
   - One Author can write multiple Books, and one Book can have multiple Authors (N:M relationship).
   - This is represented through the `BookAuthor` join table.

10. **Category**:
    - Each `Category` can be associated with multiple Books through the `BookCategory` table (1:N relationship).

---

### Additional Observations:
- **Relationships**:
  - Most relationships involve foreign keys to ensure referential integrity.
  - `JsonBackReference` annotations in the Java class suggest bidirectional relationships for efficient data serialization/deserialization.

- **Primary and Foreign Keys**:
  - Each table has clearly defined primary keys (e.g., `AdminID`, `StudentID`, `BookID`, etc.).
  - Foreign keys establish the relationships between tables.

### Misc:
* Books
  - sort by category, author, price, quantity
* Bookrequest
  - sorty by most requested books title, approve status, request date, 
* borrow record
  - sorty by most borrowed books title, return date, due date, student,
