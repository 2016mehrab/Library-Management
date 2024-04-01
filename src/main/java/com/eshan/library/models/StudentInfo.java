package com.eshan.library.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Entity
@Builder
public class StudentInfo {
    @Id
    @Column(
            name = "student_id",
            nullable = false,
            unique = true
    )
    private Integer id;

    @OneToOne(mappedBy = "studentInfo", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Student student;

    @Column(
            nullable = false,
            unique = true
    )
    private String email;
    @Column(
            nullable = false
    )
    private String name;
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;
}
