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
public class Book {
    @Id
    @Column(
            name = "book_id",
            nullable = false,
            unique = true
    )
    private  Integer id;
    private  String title;
    private  Integer quantity;
    private  Double price;
    private  String CoverLink;
    @ManyToMany
    @JoinTable(
            name = "book_author",
            joinColumns = {
                    @JoinColumn(name = "book_id")
            },
            inverseJoinColumns = {@JoinColumn
                    (name = "author_id")}
    )
    private List<Author> authors;
    @ManyToMany
    @JoinTable(
            name = "book_category",
            joinColumns = {
                    @JoinColumn(name = "book_id")
            },
            inverseJoinColumns = {@JoinColumn
                    (name = "category_id")}
    )
    private List<Category> categories;
    @OneToMany(mappedBy = "book")
    private List<BorrowRecord> borrowRecords;

    @OneToMany(mappedBy = "book")
    private List<BookRequest> bookRequests;

}
