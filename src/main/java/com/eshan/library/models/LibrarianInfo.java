package com.eshan.library.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LibrarianInfo {
    @Id
    @Column(
            name = "librarian_id",
            nullable = false,
            unique = true
    )
    private Integer id;

    @OneToOne( mappedBy = "librarianInfo",cascade = CascadeType.REMOVE)
    @JsonBackReference("librarian-librarianInfo")
    private Librarian librarian;

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
    @JsonBackReference
    private Admin admin;
}
