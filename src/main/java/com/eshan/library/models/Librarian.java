package com.eshan.library.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Entity
@Builder
public class Librarian {
        @Id
        @GeneratedValue
        @Column(
                unique = true,
                nullable = false
        )
        private Integer id;
        @Column(unique = true, nullable = false)
        private String username;
        private String password;
        @OneToMany(mappedBy = "librarian", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<BookRequest> bookRequests;
        @OneToOne
        @JoinColumn(name = "librarian_id")
        @JsonManagedReference("librarian-librarianInfo")
        private LibrarianInfo librarianInfo;
}
