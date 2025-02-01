package com.eshan.library.services.borrowRecordDTO;

public record BorrowRecordDTO(
        String isbn,
        Integer studentId,
        Integer bookRequestId) {

}
