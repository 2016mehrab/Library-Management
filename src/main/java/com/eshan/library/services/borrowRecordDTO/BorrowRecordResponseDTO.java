package com.eshan.library.services.borrowRecordDTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class BorrowRecordResponseDTO
{

        Integer borrowRecordId;
        Integer studentId;
        Integer librarianId;
        String isbn;
        Integer bookRequestId;
        Double fine;
        Boolean isLost;
        LocalDateTime borrowDate;
        LocalDateTime dueDate;
        LocalDateTime returnDate;

}