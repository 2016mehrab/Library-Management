package com.eshan.library.repositories;

import com.eshan.library.models.BookRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRequestRepository extends JpaRepository<BookRequest,Integer> {
}
