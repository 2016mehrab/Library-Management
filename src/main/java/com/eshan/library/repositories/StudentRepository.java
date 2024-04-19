package com.eshan.library.repositories;

import com.eshan.library.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


public interface StudentRepository extends JpaRepository<Student,Integer> {
    Optional<Student> findByUsername(String username);
}
