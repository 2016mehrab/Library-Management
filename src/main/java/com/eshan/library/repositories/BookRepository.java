package com.eshan.library.repositories;

import com.eshan.library.models.Book;

import jakarta.transaction.Transactional;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.data.repository.query.Param;


public interface BookRepository extends ListPagingAndSortingRepository<Book, Integer>,CrudRepository<Book, Integer> {
    Optional<Book> findByIsbn(String isbn);

    @Modifying
    @Transactional
    @Query("delete from Book b where b.isbn=?1")
    void deleteByIsbn(String isbn);

    @Modifying
    @Query("UPDATE Book b SET b.quantity = b.quantity - 1 WHERE b.id = :id AND b.quantity > 0")
    int decrementQuantity(@Param("id") Integer id);

    @Modifying
    @Query("UPDATE Book b SET b.quantity = b.quantity + 1 WHERE b.id = :bookId")
    int incrementQuantity(@Param("bookId") Integer bookId);
}
