package com.eshan.library.repositories;

import com.eshan.library.models.Book;

import jakarta.transaction.Transactional;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {
    Optional<Book> findByIsbn(String isbn);

    @Modifying
    @Transactional
    @Query("delete from Book b where b.isbn=?1")
    void deleteByIsbn(String isbn);
}
