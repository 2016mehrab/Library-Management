package com.eshan.library.services.bookRequestDTO;

import com.eshan.library.models.ApproveStatus;
import com.eshan.library.models.Book;
import com.eshan.library.models.Librarian;
import com.eshan.library.models.Student;

public record BookRequestDTO(

    Integer librarianId,
    Integer studentId,
    String isbn
) {

}
