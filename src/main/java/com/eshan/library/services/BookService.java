package com.eshan.library.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.eshan.library.models.Author;
import com.eshan.library.models.Book;
import com.eshan.library.models.Category;
import com.eshan.library.repositories.AuthorRepository;
import com.eshan.library.repositories.BookRepository;
import com.eshan.library.repositories.CategoryRepository;
import com.eshan.library.services.bookDTO.BookDTO;
import com.eshan.library.services.bookDTO.BookResponseDTO;
import com.eshan.library.services.bookDTO.BookUpdateDTO;
import com.eshan.library.services.bookDTO.CoverLinkUpdateDTO;
import com.eshan.library.services.bookDTO.QuantityUpdateDTO;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final AuthorRepository authorRepository;

    public Book save(BookDTO dto) {
        var book = toBook(dto);
        Optional<Book> optionalBook = bookRepository.findByIsbn(book.getIsbn());
        if (!optionalBook.isPresent()) {
            return bookRepository.save(book);
        } else {
            throw new RuntimeException("Operation was not successful: Book exists!");
        }

    }

    private Book toBook(BookDTO dto) {
        var book = new Book();
        book.setIsbn(dto.isbn());
        book.setTitle(dto.title());
        book.setPrice(dto.price());
        book.setQuantity(dto.quantity());
        book.setCoverLink(dto.coverLink());

        List<Category> categories = dto.categories().stream()
                .map(name -> categoryRepository.findByName(name)
                        .orElseGet(() -> {
                            Category newCategory = new Category();
                            newCategory.setName(name);
                            return categoryRepository.save(newCategory);
                        }))
                .collect(Collectors.toList());

        List<Author> authors = dto.authors().stream()
                .map(name -> authorRepository.findByName(name)
                        .orElseGet(() -> {
                            Author newAuthor = new Author();
                            newAuthor.setName(name);
                            return authorRepository.save(newAuthor);
                        }))
                .collect(Collectors.toList());

        book.setAuthors(authors);
        book.setCategories(categories);
        return book;

    }

    private BookResponseDTO toBookResponseDTO(Book book) {
        return new BookResponseDTO(
                book.getIsbn(),
                book.getTitle(),
                book.getQuantity(),
                book.getPrice(),
                book.getCoverLink(),
                book.getCategories().stream().map(Category::getName).collect(Collectors.toList()),
                book.getAuthors().stream().map(Author::getName).collect(Collectors.toList()));
    }

    public BookResponseDTO findByIsbn(String isbn) {
        Optional<Book> optionalBook = bookRepository.findByIsbn(isbn);
        if (optionalBook.isPresent()) {
            return toBookResponseDTO(optionalBook.get());
        } else {
            throw new RuntimeException("Operation was not successful: Book does not exist!");
        }
    }

    public Page<BookResponseDTO> findAll(int pageNum, int pageSize, List<String> sortby, Direction dir) {
        Sort sort = Sort.by(dir, sortby.toArray(new String[0]));
        Pageable pg = PageRequest.of(pageNum, pageSize, sort);
        return  bookRepository.findAll(pg).map(this::toBookResponseDTO);
    }

    // public Book findByIsbn(String isbn) {
    // Optional<Book> optionalBook = bookRepository.findByIsbn(isbn);
    // if (optionalBook.isPresent()) {
    // return optionalBook.get();
    // } else {
    // throw new RuntimeException("Operation was not successful: Book does not
    // exist!");
    // }

    // }

    // public List<Book> findAll() {
    // return bookRepository.findAll();
    // }


    public boolean delete(String isbn) {
        if (!bookRepository.findByIsbn(isbn).isPresent()) {
            return false; // Book not found
        }

        bookRepository.deleteByIsbn(isbn);
        return true;
    }

    public void updateQuantity(QuantityUpdateDTO quantityDTO, String isbn) {
        Optional<Book> optionalBook = bookRepository.findByIsbn(isbn);
        if (optionalBook.isPresent()) {
            optionalBook.get().setQuantity(quantityDTO.quantity());
            bookRepository.save(optionalBook.get());
        } else {
            throw new RuntimeException("Operation was not successful: Book does not exist!");
        }
    }

    public void updateCoverLink(CoverLinkUpdateDTO linkUpdateDTO, String isbn) {
        Optional<Book> optionalBook = bookRepository.findByIsbn(isbn);
        if (optionalBook.isPresent()) {
            optionalBook.get().setCoverLink(linkUpdateDTO.coverLink());
            bookRepository.save(optionalBook.get());

        } else {
            throw new RuntimeException("Operation was not successful: Book does not exist!");
        }
    }

    public void updateBook(String isbn, BookUpdateDTO bookUpdateDTO) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        // Update fields if they are not null in the DTO
        if (bookUpdateDTO.title() != null) {
            book.setTitle(bookUpdateDTO.title());
        }
        if (bookUpdateDTO.quantity() != null) {
            book.setQuantity(bookUpdateDTO.quantity());
        }
        if (bookUpdateDTO.price() != null) {
            book.setPrice(bookUpdateDTO.price());
        }
        if (bookUpdateDTO.coverLink() != null) {
            book.setCoverLink(bookUpdateDTO.coverLink());
        }
        if (bookUpdateDTO.categories() != null) {
            List<Category> categories = bookUpdateDTO.categories().stream()
                    .map(name -> categoryRepository.findByName(name)
                            .orElseGet(() -> {
                                Category newCategory = new Category();
                                newCategory.setName(name);
                                return categoryRepository.save(newCategory);
                            }))
                    .collect(Collectors.toList());

            book.setCategories(categories);
        }
        if (bookUpdateDTO.authors() != null) {
            List<Author> authors = bookUpdateDTO.authors().stream()
                    .map(name -> authorRepository.findByName(name)
                            .orElseGet(() -> {
                                Author newAuthor = new Author();
                                newAuthor.setName(name);
                                return authorRepository.save(newAuthor);
                            }))
                    .collect(Collectors.toList());
            book.setAuthors(authors);
        }

        bookRepository.save(book);
    }

}
