package com.eshan.library.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Entity
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")

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
