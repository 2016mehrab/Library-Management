package com.eshan.library.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Entity
@Builder
public class Librarian {
    @Id
    @Column(
            name = "librarian_id"
    )
    private Integer id;
    @Column(
            unique = true,
            nullable = false
    )
    private String username;
    private String password;
    @OneToMany(mappedBy = "librarian")
    private List<BookRequest> bookRequests;
}
