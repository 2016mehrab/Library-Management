package com.eshan.library.services;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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

    // private BookRequestResponseDTO toBookRequestResponseDTO(BookRequestDTO
    // bookRequestDTO) {
    // Optional<Book> book = bookRepository.findByIsbn(bookRequestDTO.isbn());
    // Optional<Student> student =
    // studentRepository.findById(bookRequestDTO.studentId());
    // Librarian librarian =
    // librarianService.findLibrarianById(bookRequestDTO.librarianId());

    // if (book.isPresent() && student.isPresent() && librarian != null) {
    // return BookRequestResponseDTO.builder().isbn(bookRequestDTO.isbn())
    // .librarianId(bookRequestDTO.librarianId())
    // .studentId(bookRequestDTO.studentId())
    // .status(book.ge)
    // .build();
    // }
    // else{
    // throw new RuntimeException("Operation was not successful: Librarian Not
    // Found!");
    // }
    // }

    private BookRequestResponseDTO toBookRequestResponseDTO(BookRequest br) {
        Optional<Book> book = bookRepository.findByIsbn(br.getBook().getIsbn());
        Optional<Student> student = studentRepository.findById(br.getStudent().getId());
        Librarian librarian = librarianService.findLibrarianById(br.getLibrarian().getLibrarianInfo().getId());

        if (book.isPresent() && student.isPresent() && librarian != null) {
            return BookRequestResponseDTO.builder().isbn(book.get().getIsbn())
                    .librarianId(librarian.getLibrarianInfo().getId())
                    .studentId(student.get().getId())
                    .id(br.getId())
                    .status(br.getApproveStatus())
                    .build();
        } else
            return null;
    }

    // public List<BookRequest> findAll() {
    // return bookRequestRepository.findAll();

    // }
    public List<BookRequestResponseDTO> findAll() {
        return bookRequestRepository.findAll().stream().map(this::toBookRequestResponseDTO)
                .collect(Collectors.toList());

    }

    public List<BookRequestResponseDTO> getRequestsForLibrarian(Integer librarianId) {
        Optional<Librarian> lb = librarianRepository.findByLibrarianInfo_Id(librarianId);
        if (lb.isPresent()) {
            return bookRequestRepository.findByLibrarian(lb.get()).stream().map(this::toBookRequestResponseDTO)
                    .collect(Collectors.toList());

        } else {
            throw new RuntimeException("Operation was not successful: Librarian does not exist");
        }

    }

    public List<BookRequestResponseDTO> getRequestsForStudent(Integer studentid) {
        Optional<Student> st = studentRepository.findById(studentid);
        if (st.isPresent()) {
            return bookRequestRepository.findByStudent(st.get()).stream().map(this::toBookRequestResponseDTO)
                    .collect(Collectors.toList());

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

    public void delete(Integer id) {
        bookRequestRepository.deleteById(id);
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
