package com.eshan.library.services.bookDTO;

import java.util.List;

public record BookUpdateDTO(
        String title,
        Integer quantity,
        Double price,
        String coverLink,
        List<String> categories,
        List<String> authors) {

}
