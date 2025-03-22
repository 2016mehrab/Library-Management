package com.eshan.library.repositories;

import com.eshan.library.models.Librarian;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface LibrarianRepository extends JpaRepository<Librarian, Integer> {
    Optional<Librarian> findByLibrarianInfo_Id(Integer librarianId);

    Optional<Librarian> findByUsername(String username);
}
