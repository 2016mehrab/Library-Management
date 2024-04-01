package com.eshan.library.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Entity
@Builder
public class BorrowRecord {
    //    TODO: Add studentId foreign key
    //    TODO: Add bookId foreign key
    //    TODO: Add librarianId foreign key
    //    TODO: Add requestId foreign key
    @Id
    @GeneratedValue
    @Column(
            name = "borrow_record_id",
            unique = true,
            nullable = false
    )
    private Integer id;
    private Double fine;
    private Boolean isLost;
    @Column(
            name = "borrow_date",
            insertable = true,
            updatable = false,
            nullable = false
    )
    private LocalDateTime borrowDate;
    @Column(
            name = "due_date",
            insertable = true,
            updatable = false,
            nullable = false
    )
    private LocalDateTime dueDate;
    @Column(
            name = "return_date",
            insertable = false,
            updatable = true,
            nullable = false
    )
    private LocalDateTime returnDate;
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

}
