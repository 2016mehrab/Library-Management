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
public class Category {
    @Id
    @GeneratedValue
    @Column(
            name = "category_id",
            nullable = false,
            unique = true
    )
    private Integer id;
    @Column(
            name = "category_name",
            nullable = false
    )
    private String name;
    @ManyToMany(mappedBy = "categories")
    private List<Book> books;
}
