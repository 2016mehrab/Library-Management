package com.eshan.library.services;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.eshan.library.models.ApproveStatus;
import com.eshan.library.models.Book;
import com.eshan.library.models.BookRequest;
import com.eshan.library.models.Librarian;
import com.eshan.library.models.LibrarianInfo;
import com.eshan.library.models.Student;
import com.eshan.library.models.StudentInfo;
import com.eshan.library.repositories.BookRepository;
import com.eshan.library.repositories.BookRequestRepository;
import com.eshan.library.repositories.LibrarianInfoRepository;
import com.eshan.library.repositories.LibrarianRepository;
import com.eshan.library.repositories.StudentInfoRepository;
import com.eshan.library.repositories.StudentRepository;
import com.eshan.library.services.bookRequestDTO.BookRequestDTO;
import com.eshan.library.services.bookRequestDTO.BookRequestResponseDTO;
import com.eshan.library.services.bookRequestDTO.StatusUpdateDTO;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BookRequestService {
    private final BookRepository bookRepository;
    private final BookRequestRepository bookRequestRepository;
    private final LibrarianInfoRepository librarianInfoRepository;
    private final StudentInfoRepository studentInfoRepository;
    private final StudentRepository studentRepository;
    private final LibrarianRepository librarianRepository;
    private final LibrarianService librarianService;

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

    private BookRequestResponseDTO toBookRequestResponseDTO(BookRequest br) {
        Optional<Book> book = bookRepository.findByIsbn(br.getBook().getIsbn());
        Optional<Student> student = studentRepository.findById(br.getStudent().getId());
        // Librarian librarian = librarianService.findLibrarianById(br.getLibrarian().getLibrarianInfo().getId());
        Optional<Librarian> librarian = librarianRepository.findById(br.getLibrarian().getId());

        if (book.isPresent() && student.isPresent() && librarian .isPresent()) {
            return BookRequestResponseDTO.builder().isbn(book.get().getIsbn())
                    .librarianId(librarian.get().getId())
                    .studentId(student.get().getId())
                    .id(br.getId())
                    .status(br.getApproveStatus())
                    .requestDate(br.getRequestDate())
                    .build();
        } else
            return null;
    }


    public Page<BookRequestResponseDTO> findAll(Integer pageNumber, Integer pageSize) {
        var page= bookRequestRepository.findAll(PageRequest.of(pageNumber, pageSize));
        return page.map(this::toBookRequestResponseDTO);

        // return bookRequestRepository.findAll().stream().map(this::toBookRequestResponseDTO)
                // .collect(Collectors.toList());

    }

    public Page<BookRequestResponseDTO> getRequestsForLibrarian(Integer librarianId, Integer pageNumber, Integer pageSize) {
        Optional<Librarian> lb = librarianRepository.findById(librarianId);
        if (lb.isPresent()) {
            var page = bookRequestRepository.findByLibrarian(lb.get(), PageRequest.of(pageNumber, pageSize));
            return page.map(this::toBookRequestResponseDTO);
            // return bookRequestRepository.findByLibrarian(lb.get()).stream().map(this::toBookRequestResponseDTO)
                    // .collect(Collectors.toList());
        } else {
            throw new RuntimeException("Operation was not successful: Librarian does not exist");
        }

    }

    public Page<BookRequestResponseDTO> getRequestsForStudent(Integer studentid, Integer pageNumber, Integer pageSize) {
        Optional<Student> st = studentRepository.findById(studentid);
        if (st.isPresent()) {
            var page = bookRequestRepository.findByStudent(st.get(), PageRequest.of(pageNumber,pageSize ));
            return page.map(this::toBookRequestResponseDTO);
            // return bookRequestRepository.findByStudent(st.get()).stream().map(this::toBookRequestResponseDTO)
            //         .collect(Collectors.toList());

        } else {
            throw new RuntimeException("Operation was not successful: student does not exist");
        }

    }

    public BookRequestResponseDTO findById(Integer id) {
        if (bookRequestRepository.findById(id).isPresent()) {
            return toBookRequestResponseDTO(bookRequestRepository.findById(id).get());
        } else {
            throw new RuntimeException("Operation was not successful: Book request doesnot exist");
        }

    }

    public boolean delete(Integer id) {

        var exists = bookRequestRepository.existsById(id);
        if (exists) {
            bookRequestRepository.deleteById(id);
            return true;
        } else
            return false;
    }

    public void updateStatus(StatusUpdateDTO dto, Integer id) {
        BookRequest bookRequest = bookRequestRepository.findById(id).orElse(null);
        if (bookRequest != null) {
            if (dto.approveStatus() == ApproveStatus.APPROVED && bookRequest.getBook().getQuantity() > 0) {
                // set status
                bookRequest.setApproveStatus(dto.approveStatus());
                // save to db
                bookRequestRepository.save(bookRequest);
            } else if (dto.approveStatus() != ApproveStatus.APPROVED) {
                // set status
                bookRequest.setApproveStatus(dto.approveStatus());
                // save to db
                bookRequestRepository.save(bookRequest);
            } else {
                throw new RuntimeException("Operation was not successful: Book not available");
            }

        } else {
            throw new RuntimeException("Operation was not successful: BookRequest does not exist!");
        }
    }

}
