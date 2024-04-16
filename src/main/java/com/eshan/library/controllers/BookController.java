package com.eshan.library.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.eshan.library.models.Book;
import com.eshan.library.services.BookService;
import com.eshan.library.services.bookDTO.BookDTO;
import com.eshan.library.services.bookDTO.CoverLinkUpdateDTO;
import com.eshan.library.services.bookDTO.QuantityUpdateDTO;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/books")
@AllArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping
    public ResponseEntity<String> post(@RequestBody BookDTO dto) {
        try {
            bookService.save(dto);
            return new ResponseEntity<>(null, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("{Id}")
    public Book findById(@PathVariable("Id") String isbn) {
        return bookService.findByIsbn(isbn);
    }

    // TODO: Implement response DTO
    @GetMapping
    public List<Book> findAll() {

        return bookService.findAll();
    }

    @DeleteMapping("{Id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLibrarian(@PathVariable("Id") String isbn) {
        bookService.delete(isbn);
    }

    @PutMapping("{Id}/quantity")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Book> updateQuatity(@RequestBody QuantityUpdateDTO quantityDto,
            @PathVariable("Id") String isbn) {
        try {
            bookService.updateQuantity(quantityDto, isbn);
            return new ResponseEntity<>(null, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("{Id}/coverlink")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Book> updateCoverLink(@RequestBody CoverLinkUpdateDTO linkUpdateDTO,
            @PathVariable("Id") String isbn) {
        try {
            bookService.updateCoverLink(linkUpdateDTO, isbn);
            return new ResponseEntity<>(null, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
