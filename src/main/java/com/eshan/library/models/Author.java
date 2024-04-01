package com.eshan.library.models;

import jakarta.persistence.*;
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
public class Author {
    @Id
    @GeneratedValue
    @Column(
            name = "author_id",
            nullable = false,
            unique = true
    )
    private Integer id;
    @Column(
            name = "author_name",
            nullable = false
    )
    private String name;
    @ManyToMany(mappedBy = "authors")
    private List<Book> books;
}
