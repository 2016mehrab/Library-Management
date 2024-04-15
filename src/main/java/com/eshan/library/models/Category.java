package com.eshan.library.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Entity
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Category {
        @Id
        @GeneratedValue
        @Column(name = "category_id", nullable = false, unique = true)
        private Integer id;
        @Column(name = "category_name", nullable = false)
        private String name;

        @ManyToMany(mappedBy = "categories")
        private List<Book> books;

}
