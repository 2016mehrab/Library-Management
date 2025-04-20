package com.eshan.library.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.eshan.library.models.ApproveStatus;
import com.eshan.library.models.Book;
import com.eshan.library.models.BookRequest;
import com.eshan.library.models.BorrowRecord;
import com.eshan.library.models.Librarian;
import com.eshan.library.models.Student;
import com.eshan.library.models.StudentInfo;
import com.eshan.library.repositories.BookRepository;
import com.eshan.library.repositories.BookRequestRepository;
import com.eshan.library.repositories.BorrowRecordRepository;
import com.eshan.library.repositories.LibrarianRepository;
import com.eshan.library.repositories.StudentInfoRepository;
import com.eshan.library.repositories.StudentRepository;
import com.eshan.library.services.borrowRecordDTO.BorrowRecordDTO;
import com.eshan.library.services.borrowRecordDTO.BorrowRecordResponseDTO;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BorrowRecordService {
    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;
    private final StudentInfoRepository studentInfoRepository;
    private final LibrarianRepository librarianRepository;
    private final StudentRepository studentRepository;
    private final BookRequestRepository bookRequestRepository;

    public BorrowRecord save(BorrowRecordDTO dto) {
        var borrowRecord = toBorrowRecord(dto);
        var bookRequest = bookRequestRepository.findById(dto.bookRequestId());
        // book quantity reduce
        var currQuantity= borrowRecord.getBook().getQuantity();
        borrowRecord.getBook().setQuantity(currQuantity-1);

        if (bookRequest.isPresent() && bookRequest.get().getApproveStatus() == ApproveStatus.APPROVED) {
            return borrowRecordRepository.save(borrowRecord);
        } else {
            throw new RuntimeException("Operation was not successful: Book-request was not approved!");

        }

    }

    private BorrowRecord toBorrowRecord(BorrowRecordDTO dto) {
        var borrowRecord = new BorrowRecord();
        Book b = bookRepository.findByIsbn(dto.isbn()).orElse(null);

        if (b != null) {
            borrowRecord.setBook(b);
        } else {
            throw new RuntimeException("Operation was not successful: Book not found!");
        }

        StudentInfo SI = studentInfoRepository.findById(dto.studentId()).orElse(null);
        if (SI != null) {
            Student S = SI.getStudent();
            if (S != null) {
                borrowRecord.setStudent(S);
            } else {
                throw new RuntimeException("Operation was not successful: Student not found!");
            }
        }
        BookRequest BR = bookRequestRepository.findById(dto.bookRequestId()).orElse(null);
        if (BR != null) {
            borrowRecord.setBookRequest(BR);
        } else {
            throw new RuntimeException("Operation was not successful: BookRequest does not exist!");
        }
        return borrowRecord;
    }
    private BorrowRecordResponseDTO toBorrowRecordResponseDTO(BorrowRecord br){
        Optional<Book> book = bookRepository.findByIsbn(br.getBook().getIsbn());
        Optional<Student> student =studentRepository.findById(br.getStudent().getId());
        Optional<Librarian> librarian = librarianRepository.findById(br.getLibrarian().getId());
        Optional<BookRequest> brq =bookRequestRepository.findById(br.getBookRequest().getId());
        if(book.isPresent() && librarian.isPresent() && student.isPresent() && brq.isPresent()){
            return BorrowRecordResponseDTO.builder()
            .isbn(book.get().getIsbn())
            .returnDate(br.getReturnDate())
            .isLost(br.getIsLost())
            .fine(br.getFine())
            .studentId(student.get().getId())
            .librarianId(librarian.get().getId())
            .borrowRecordId(br.getId())
            .dueDate(br.getDueDate())
            .borrowDate(br.getBorrowDate())
            .bookRequestId(brq.get().getId())
            .build();
        }
        else
            return null;

    }

    public Page<BorrowRecordResponseDTO> findAll(Integer pageNumber,Integer pageSize ) {
        return borrowRecordRepository.findAll(PageRequest.of(pageNumber, pageSize)).map(this::toBorrowRecordResponseDTO);
    }

    public void delete(Integer id) {
        borrowRecordRepository.deleteById(id);
    }

    public void returnBook(Integer id) {
        BorrowRecord borrowRecord = borrowRecordRepository.findById(id).orElse(null);
        if (borrowRecord != null) {
        // book quantity increase
            var currQuantity= borrowRecord.getBook().getQuantity();
            borrowRecord.getBook().setQuantity(currQuantity);
        // book returned now
            borrowRecord.setReturnDate(LocalDateTime.now());
            borrowRecordRepository.save(borrowRecord);
        } else {
            throw new RuntimeException("Operation was not successful: BookRecord does not exist!");
        }
    }

    public void bookLost(Integer id) {
        BorrowRecord borrowRecord = borrowRecordRepository.findById(id).orElse(null);
        if (borrowRecord != null) {
            borrowRecord.setIsLost(true);
            borrowRecord.setReturnDate(LocalDateTime.now());
            calculatefine(borrowRecord);
            borrowRecordRepository.save(borrowRecord);
            Logger.getLogger("BookLost").info(""+borrowRecord);
            borrowRecordRepository.flush();
        } else {
            throw new RuntimeException("Operation was not successful: BookRecord does not exist!");
        }
    }

    public double updateFine(Integer id) {
        BorrowRecord borrowRecord = borrowRecordRepository.findById(id).orElse(null);
        if (borrowRecord != null) {
            var fine = calculatefine(borrowRecord);
            borrowRecordRepository.save(borrowRecord);
            borrowRecordRepository.flush();
            return fine;

        } else {
            throw new RuntimeException("Operation was not successful: BookRecord does not exist!");
        }
    }

    public double calculatefine(BorrowRecord borrowRecord) {
        long daysBetween = this.daysBetween(borrowRecord);
        Logger.getLogger("BookLost").info("daysbetween-> "+daysBetween);

        // initial fine
        // double fine = (borrowRecord.getFine() != null) ? borrowRecord.getFine() : 0.0;
        double fine =  0.0;
        Logger.getLogger("BookLost").info("initial fine-> "+fine);

        if (daysBetween > 0) {
            fine += daysBetween * 10;
            borrowRecord.setFine(fine);
            Logger.getLogger("BookLost").info("fine for delay-> "+fine);
        }
        if (borrowRecord.getIsLost()) {
            fine += borrowRecord.getBook().getPrice();
            Logger.getLogger("BookLost").info("fine for losing the book-> "+fine);
            borrowRecord.setFine(fine);
        }
        return fine;

    }

    public long daysBetween(BorrowRecord borrowRecord) {
        LocalDateTime dueDateTime = borrowRecord.getDueDate();
        LocalDateTime returnedDateTime = borrowRecord.getReturnDate();

        LocalDate dueDate = dueDateTime.toLocalDate();
        LocalDate returnedDate = returnedDateTime.toLocalDate();
        return returnedDate.toEpochDay() - dueDate.toEpochDay();

    }
}
