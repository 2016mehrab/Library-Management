package com.eshan.library.services.bookDTO;
import java.util.List;

public record BookResponseDTO(
        String isbn,
        String title,
        Integer quantity,
        Double price,
        String coverLink,
        List<String> categories,
        List<String> authors) {

}
