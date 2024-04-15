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
public class Book {
        @Id
        @GeneratedValue
        @Column(name = "book_id", nullable = false, unique = true)
        private Integer id;
        @Column(unique = true)
        private String isbn;
        private String title;
        private Integer quantity;
        private Double price;
        private String CoverLink;

        @ManyToMany(cascade = CascadeType.ALL)
        @JoinTable(name = "book_author", joinColumns = {
                        @JoinColumn(name = "book_id")
        }, inverseJoinColumns = { @JoinColumn(name = "author_id") })
        
        private List<Author> authors;
        // owner of the relationship
        @ManyToMany
        @JoinTable(name = "book_category", joinColumns = {
                        @JoinColumn(name = "book_id")
        }, inverseJoinColumns = { @JoinColumn(name = "category_id") })
        private List<Category> categories;

        @OneToMany(mappedBy = "book")
        private List<BorrowRecord> borrowRecords;

        @OneToMany(mappedBy = "book")
        private List<BookRequest> bookRequests;

}
