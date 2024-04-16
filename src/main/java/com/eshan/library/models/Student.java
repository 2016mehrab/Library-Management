package com.eshan.library.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Entity
@Builder
public class Student {
        @Id
        @Column(name = "student_id")
        private Integer id;
        @OneToOne
        @MapsId
        @JoinColumn(name = "student_id", referencedColumnName = "id")
        @JsonManagedReference("student-studentInfo")
        private StudentInfo studentInfo;
        @Column(unique = true, nullable = false)
        private String username;
        private String password;
        @OneToMany(mappedBy = "student")
        @JsonManagedReference("student-borrowrecord")
        private List<BorrowRecord> borrowRecords;

        @OneToMany(mappedBy = "student")
        @JsonManagedReference("bookrequest-student")
        private List<BookRequest> bookRequests;
}
