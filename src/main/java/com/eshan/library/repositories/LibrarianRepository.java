package com.eshan.library.repositories;

import com.eshan.library.models.Librarian;


import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface LibrarianRepository extends JpaRepository<Librarian,Integer> {
    Librarian findByLibrarianInfo_Id(Integer librarianId);
}
