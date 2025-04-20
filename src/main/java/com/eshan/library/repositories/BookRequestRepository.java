package com.eshan.library.repositories;

import com.eshan.library.models.BookRequest;
import com.eshan.library.models.Librarian;
import com.eshan.library.models.Student;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRequestRepository extends JpaRepository<BookRequest, Integer> {
    Page<BookRequest> findByLibrarian(Librarian librarian, Pageable pageable);

    Page<BookRequest> findByStudent(Student student, Pageable pageable);
}
