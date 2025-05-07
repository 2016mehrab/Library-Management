package com.eshan.library.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Logger;

import com.eshan.library.constants.Constants;
import com.eshan.library.services.bookRequestDTO.BookRequestResponseDTO;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class BorrowRecordService {
    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;
    private final StudentInfoRepository studentInfoRepository;
    private final LibrarianRepository librarianRepository;
    private final StudentRepository studentRepository;
    private final BookRequestRepository bookRequestRepository;

    @Transactional
    public BorrowRecord save(BorrowRecordDTO dto) {
        var borrowRecord = toBorrowRecord(dto);
        var bookRequest = bookRequestRepository.findById(dto.bookRequestId());

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

        var librarian = BR.getLibrarian();
        if (librarian != null) {
            borrowRecord.setLibrarian(librarian);
        } else {
            throw new RuntimeException("Operation was not successful: Librarian does not exist!");
        }
        return borrowRecord;
    }

    private BorrowRecordResponseDTO toBorrowRecordResponseDTO(BorrowRecord br){
        Optional<Book> book = bookRepository.findByIsbn(br.getBook().getIsbn());
        Optional<Student> student =studentRepository.findById(br.getStudent().getId());
        Optional<Librarian> librarian = librarianRepository.findById(br.getLibrarian().getProfileId());
        Optional<BookRequest> brq =bookRequestRepository.findById(br.getBookRequest().getId());

        if(book.isPresent() && librarian.isPresent() && student.isPresent() && brq.isPresent()){
            return BorrowRecordResponseDTO.builder()
            .isbn(book.get().getIsbn())
            .returnDate(br.getReturnDate())
            .isLost(br.getIsLost())
            .fine(br.getFine())
            .studentId(student.get().getId())
            .librarianId(librarian.get().getProfileId())
            .borrowRecordId(br.getId())
            .dueDate(br.getDueDate())
            .borrowDate(br.getBorrowDate())
            .bookRequestId(brq.get().getId())
            .build();
        }
        else
            return null;

    }

    @Transactional(readOnly = true)
    public Page<BorrowRecordResponseDTO> findAll(Integer pageNumber,Integer pageSize ) {
        return borrowRecordRepository.findAll(PageRequest.of(pageNumber, pageSize)).map(this::toBorrowRecordResponseDTO);
    }

    @Transactional
    public void delete(Integer id) {
        borrowRecordRepository.deleteById(id);
    }

    @Transactional
    public void returnBook(Integer id ) {
        BorrowRecord borrowRecord = borrowRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BorrowRecord does not exist!"));

        LocalDateTime dueDateTime = borrowRecord.getDueDate();
        LocalDateTime returnedDateTime = LocalDateTime.now();
        LocalDate dueDate = dueDateTime.toLocalDate();
        LocalDate returnedDate = returnedDateTime.toLocalDate();
        long daysBetween = returnedDate.toEpochDay() - dueDate.toEpochDay();

        double fine =  0.0;
        if (daysBetween > 0) {
            fine += daysBetween * Constants.FINE_PER_DAY;
            borrowRecord.setFine(fine);
        }
        int updated = bookRepository.incrementQuantity(borrowRecord.getBook().getId());
        if (updated == 0) {
            throw new RuntimeException("Failed to update book quantity");
        }
        borrowRecord.setReturnDate(returnedDateTime);
        borrowRecordRepository.save(borrowRecord);
    }

    @Transactional
    public void testReturnBook(Integer id,Integer delay ) {
        BorrowRecord borrowRecord = borrowRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BorrowRecord does not exist!"));

        LocalDateTime dueDateTime = borrowRecord.getDueDate();
        LocalDateTime returnedDateTime = borrowRecord.getBorrowDate().plusDays(delay) ;
        LocalDate dueDate = dueDateTime.toLocalDate();
        LocalDate returnedDate = returnedDateTime.toLocalDate();
        long daysBetween = returnedDate.toEpochDay() - dueDate.toEpochDay();

        double fine =  0.0;
        if (daysBetween > 0) {
            fine += daysBetween * Constants.FINE_PER_DAY;
            borrowRecord.setFine(fine);
        }
        int updated = bookRepository.incrementQuantity(borrowRecord.getBook().getId());
        if (updated == 0) {
            throw new RuntimeException("Failed to update book quantity");
        }
        borrowRecord.setReturnDate(returnedDateTime);
        borrowRecordRepository.save(borrowRecord);
    }

    @Transactional(readOnly = true)
    public Page<BorrowRecordResponseDTO> findOngoingBorrows(Integer pageNumber,Integer pageSize) {
        return borrowRecordRepository.findOngoingBorrowRecords(PageRequest.of(pageNumber, pageSize)).map(this::toBorrowRecordResponseDTO);
    }
    @Transactional(readOnly = true)
    public Page<BorrowRecordResponseDTO> getBorrowHistory(Integer pageNumber,Integer pageSize) {
        return borrowRecordRepository.findBorrowHistory(PageRequest.of(pageNumber, pageSize)).map(this::toBorrowRecordResponseDTO);
    }


    @Transactional(readOnly = true)
    public Page<BorrowRecordResponseDTO> getOngoingForStudent(Integer studentId, Integer pageNumber, Integer pageSize) {
        Optional<Student> st = studentRepository.findById(studentId);
        if (st.isPresent()) {
            var page = borrowRecordRepository.findOngoingBorrowRecordsByStudentId(st.get().getId(), PageRequest.of(pageNumber,pageSize ));
            return page.map(this::toBorrowRecordResponseDTO);
        } else {
            throw new RuntimeException("Operation was not successful: student does not exist");
        }
    }

    @Transactional(readOnly = true)
    public Page<BorrowRecordResponseDTO> getHistoryForStudent(Integer studentId, Integer pageNumber, Integer pageSize) {
        Optional<Student> st = studentRepository.findById(studentId);
        if (st.isPresent()) {
            var page = borrowRecordRepository.findBorrowRecordsHistoryByStudentId(st.get().getId(), PageRequest.of(pageNumber,pageSize ));
            return page.map(this::toBorrowRecordResponseDTO);
        } else {
            throw new RuntimeException("Operation was not successful: student does not exist");
        }
    }

    @Transactional
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

    @Transactional
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
