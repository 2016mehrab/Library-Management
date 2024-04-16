package com.eshan.library.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;

import com.eshan.library.models.Book;
import com.eshan.library.models.BookRequest;
import com.eshan.library.models.BorrowRecord;
import com.eshan.library.models.Student;
import com.eshan.library.models.StudentInfo;
import com.eshan.library.repositories.BookRepository;
import com.eshan.library.repositories.BookRequestRepository;
import com.eshan.library.repositories.BorrowRecordRepository;
import com.eshan.library.repositories.StudentInfoRepository;
import com.eshan.library.services.borrowRecordDTO.BorrowRecordDTO;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BorrowRecordService {
    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;
    private final StudentInfoRepository studentInfoRepository;
    private final BookRequestRepository bookRequestRepository;

    public BorrowRecord save(BorrowRecordDTO dto) {
        var borrowRecord = toBorrowRecord(dto);
        return borrowRecordRepository.save(borrowRecord);
    }

    private BorrowRecord toBorrowRecord(BorrowRecordDTO dto) {
        var borrowRecord = new BorrowRecord();
        Book b = bookRepository.findById(dto.bookId()).orElse(null);
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
            borrowRecord.setBookRequest(null);
        } else {
            throw new RuntimeException("Operation was not successful: BookRequest does not exist!");
        }
        return borrowRecord;
    }

    public List<BorrowRecord> findAll() {
        return borrowRecordRepository.findAll();
    }

    public void delete(Integer id) {
        borrowRecordRepository.deleteById(id);
    }

    public void returnBook(Integer id) {
        BorrowRecord borrowRecord = borrowRecordRepository.findById(id).orElse(null);
        if (borrowRecord != null) {
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
            borrowRecordRepository.save(borrowRecord);
        } else {
            throw new RuntimeException("Operation was not successful: BookRecord does not exist!");
        }
    }

    public double updateFine(Integer id) {
        BorrowRecord borrowRecord = borrowRecordRepository.findById(id).orElse(null);
        if (borrowRecord != null) {
            LocalDateTime dueDateTime = borrowRecord.getDueDate();
            LocalDateTime returnedDateTime = borrowRecord.getReturnDate();

            LocalDate dueDate = dueDateTime.toLocalDate();
            LocalDate returnedDate = returnedDateTime.toLocalDate();

            long daysBetween = returnedDate.toEpochDay() - dueDate.toEpochDay();
            double fine = 0;
            if (daysBetween > 0) {
                fine = daysBetween * 10; 
                borrowRecord.setFine(fine);
            }
            borrowRecordRepository.save(borrowRecord);
            return fine;

        } else {
            throw new RuntimeException("Operation was not successful: BookRecord does not exist!");
        }
    }
}
