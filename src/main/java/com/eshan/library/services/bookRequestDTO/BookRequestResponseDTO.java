package com.eshan.library.services.bookRequestDTO;

import com.eshan.library.models.ApproveStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookRequestResponseDTO {
    private String isbn;
    private Integer id;
    private Integer studentId;
    private Integer librarianId;
    private ApproveStatus status;

}
