package com.eshan.library.models;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Entity
@Builder
public class BookRequest {
    //    TODO: Add studentId foreign key
    //    TODO: Add bookId foreign key
    //    TODO: Add librarianId foreign key
    @Id
    @GeneratedValue
    @Column(
            name = "request_id",
            unique = true,
            nullable = false
    )
    private Integer id;
    @Column(
            name = "request_date",
            insertable = true,
            updatable = false,
            nullable = false
    )
    private LocalDateTime requestDate;
    @ManyToOne
    @JoinColumn(name = "librarian_id")
    private Librarian librarian;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

//    TODO: Add approveStatus of type enum
}