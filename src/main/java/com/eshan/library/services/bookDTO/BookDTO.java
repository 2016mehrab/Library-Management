package com.eshan.library.services.bookDTO;

import java.util.List;

import com.eshan.library.models.Author;
import com.eshan.library.models.BookRequest;
import com.eshan.library.models.BorrowRecord;
import com.eshan.library.models.Category;

public record BookDTO(
        String isbn,
        String title,
        Integer quantity,
        Double price,
        String coverLink,
        List<String> categories,
        List<String> authors
        // List<Category> categories,
        // List<BookRequest> bookRequests,
        // List<BorrowRecord> borrowRecords,
        // List<Author> authors
        ) {

}
