package com.eshan.library.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.eshan.library.models.Book;
import com.eshan.library.models.BookRequest;
import com.eshan.library.models.Librarian;
import com.eshan.library.models.LibrarianInfo;
import com.eshan.library.models.Student;
import com.eshan.library.models.StudentInfo;
import com.eshan.library.repositories.BookRepository;
import com.eshan.library.repositories.BookRequestRepository;
import com.eshan.library.repositories.LibrarianInfoRepository;
import com.eshan.library.repositories.StudentInfoRepository;
import com.eshan.library.services.bookRequestDTO.BookRequestDTO;
import com.eshan.library.services.bookRequestDTO.StatusUpdateDTO;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BookRequestService {
    private final BookRepository bookRepository;
    private final BookRequestRepository bookRequestRepository;
    private final LibrarianInfoRepository librarianInfoRepository;
    private final StudentInfoRepository studentInfoRepository;

    public BookRequest save(BookRequestDTO dto) {
        var bookRequest = toBookRequest(dto);
        return bookRequestRepository.save(bookRequest);

    }

    private BookRequest toBookRequest(BookRequestDTO dto) {
        var bookRequest = new BookRequest();

        LibrarianInfo LI = librarianInfoRepository.findById(dto.librarianId()).orElse(null);
        StudentInfo SI = studentInfoRepository.findById(dto.studentId()).orElse(null);
        if (LI != null && SI != null) {
            Librarian L = LI.getLibrarian();
            Student S = SI.getStudent();
            if (L != null) {
                bookRequest.setLibrarian(L);

            } else {

                throw new RuntimeException("Operation was not successful: Librarian not found!");
            }
            if (S != null) {
                bookRequest.setStudent(S);
            } else {

                throw new RuntimeException("Operation was not successful: Student not found!");
            }
            Optional<Book> OB = bookRepository.findByIsbn(dto.isbn());
            if (!OB.isPresent()) {

                throw new RuntimeException("Operation was not successful: Book not found!");
            } else {
                bookRequest.setBook(OB.get());
            }

        } else {
            throw new RuntimeException("Operation was not successful: Student or Librarian profile does not exist!");
        }

        return bookRequest;
    }

    public List<BookRequest> findAll() {
        return bookRequestRepository.findAll();

    }

    public void delete(Integer id) {
        bookRequestRepository.deleteById(id);
    }

    public void updateStatus(StatusUpdateDTO dto, Integer id) {
        BookRequest bookRequest = bookRequestRepository.findById(id).orElse(null);
        if (bookRequest != null) {
            bookRequest.setApproveStatus(dto.approveStatus());
            bookRequestRepository.save(bookRequest);
        } else {
            throw new RuntimeException("Operation was not successful: BookRequest does not exist!");
        }
    }

}
