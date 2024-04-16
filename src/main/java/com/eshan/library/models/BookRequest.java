package com.eshan.library.models;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
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
public class BookRequest {
        @Id
        @GeneratedValue
        @Column(name = "request_id", unique = true, nullable = false)
        private Integer id;
        @Column(name = "request_date", insertable = true, updatable = false, nullable = false)
        private LocalDateTime requestDate;
        @ManyToOne
        @JoinColumn(name = "librarian_id")
        @JsonBackReference("bookrequest-librarian")
        private Librarian librarian;
        @ManyToOne
        @JoinColumn(name = "student_id")
        @JsonBackReference("bookrequest-student")
        private Student student;

        @ManyToOne
        @JoinColumn(name = "book_id")
        private Book book;

        @Enumerated(EnumType.STRING)
        @Column(name = "approve_status")
        private ApproveStatus approveStatus;

        @PrePersist
        protected void onCreate() {
                this.requestDate = LocalDateTime.now();
                this.approveStatus = ApproveStatus.PENDING;
        }

        @OneToOne(mappedBy = "bookRequest", cascade = CascadeType.REMOVE)
        @JsonBackReference("bookrequest-borrowrecord")
        private BorrowRecord borrowRecord;
}
