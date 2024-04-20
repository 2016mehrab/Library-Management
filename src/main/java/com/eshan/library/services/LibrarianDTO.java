package com.eshan.library.services;

import java.util.List;

import com.eshan.library.models.BookRequest;
import com.eshan.library.models.Role;

public record LibrarianDTO(
        String username,
        String password,
        List<BookRequest> bookRequests,
        Integer librarianId
        ) {

}
