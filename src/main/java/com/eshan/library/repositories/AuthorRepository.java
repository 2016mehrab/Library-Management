package com.eshan.library.repositories;

import com.eshan.library.models.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository <Author, Integer>{
}
