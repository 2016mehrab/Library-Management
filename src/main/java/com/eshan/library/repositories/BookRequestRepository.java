package com.eshan.library.repositories;

import com.eshan.library.models.BookRequest;
import com.eshan.library.models.Librarian;
import com.eshan.library.models.Student;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface BookRequestRepository extends JpaRepository<BookRequest,Integer> {
    List<BookRequest> findByLibrarian(Librarian librarian);
    List<BookRequest> findByStudent(Student student);;
}
