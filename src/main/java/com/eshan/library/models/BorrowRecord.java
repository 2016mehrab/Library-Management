package com.eshan.library.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Entity
@Builder
public class BorrowRecord {
        // TODO: Add studentId foreign key
        // TODO: Add bookId foreign key
        // TODO: Add librarianId foreign key
        // TODO: Add requestId foreign key
        @Id
        @GeneratedValue
        @Column(name = "borrow_record_id", unique = true, nullable = false)
        private Integer id;
        private Double fine;
        private Boolean isLost;
        @Column(name = "borrow_date", insertable = true, updatable = false, nullable = false)
        private LocalDateTime borrowDate;
        @Column(name = "due_date", insertable = true, updatable = false, nullable = false)
        private LocalDateTime dueDate;
        @Column(name = "return_date", insertable = false, updatable = true )
        private LocalDateTime returnDate;

        @ManyToOne
        @JoinColumn(name = "isbn", referencedColumnName = "isbn")
        @JsonBackReference("book-borrowrecord")
        private Book book;

        @ManyToOne
        @JoinColumn(name = "student_id")
        @JsonBackReference("student-borrowrecord")
        private Student student;

        @ManyToOne
        @JoinColumn(name = "librarian_id", referencedColumnName = "id")
        @JsonBackReference("librarian-borrowrecord")
        private Librarian librarian;

        @OneToOne
        @JoinColumn(name = "request_id")
        @JsonManagedReference("bookrequest-borrowrecord")
        private BookRequest bookRequest;

        @PrePersist
        protected void onCreate() {
                this.borrowDate = LocalDateTime.now();
                this.isLost = false;
                this.dueDate = this.borrowDate.plusDays(7);
                this.fine = 0.0;
        }

        @Override
        public String toString() {
                return "BorrowRecord [id=" + id + ", fine=" + fine + ", isLost=" + isLost + ", borrowDate=" + borrowDate
                                + ", dueDate=" + dueDate + ", returnDate=" + returnDate + ", book=" + book.getIsbn()
                                + ", student=" + student.getId() + ", librarian=" + librarian.getId() + ", bookRequest=" + bookRequest.getId()
                                + "]";
        }

 

}
