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
public class LibrarianInfo {
    @Id
    @Column(
            name = "librarian_id",
            nullable = false,
            unique = true
    )
    private Integer id;
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
