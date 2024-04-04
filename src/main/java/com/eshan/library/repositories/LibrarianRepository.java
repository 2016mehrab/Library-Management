package com.eshan.library.repositories;

import com.eshan.library.models.Librarian;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibrarianRepository extends JpaRepository<Librarian,Integer> {
}
