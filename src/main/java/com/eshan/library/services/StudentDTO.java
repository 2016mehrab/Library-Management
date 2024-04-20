package com.eshan.library.services;

import java.util.List;

import com.eshan.library.models.BookRequest;
import com.eshan.library.models.BorrowRecord;
import com.eshan.library.models.Role;

public record StudentDTO(
        String username,
        String password,
        List<BookRequest> bookRequests,
        List<BorrowRecord> borrowRecords,
        Integer studentId
        ) {

}
