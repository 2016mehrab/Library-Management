package com.eshan.library.controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eshan.library.models.BorrowRecord;
import com.eshan.library.services.BorrowRecordService;
import com.eshan.library.services.borrowRecordDTO.BorrowRecordDTO;
import com.eshan.library.services.borrowRecordDTO.BorrowRecordResponseDTO;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/borrowrecords")
@AllArgsConstructor
@CrossOrigin
public class BorrowRecordController {
    private final BorrowRecordService borrowRecordService;

    @PostMapping
    public ResponseEntity<String> post(@RequestBody BorrowRecordDTO dto) {
        try {
            borrowRecordService.save(dto);
            return new ResponseEntity<>(null, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            // return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public Page<BorrowRecordResponseDTO> findAll(
            @RequestParam(name = "page", defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize

    ) {
        return borrowRecordService.findAll(pageNumber, pageSize);
    }

    @GetMapping("/ongoing")
    public Page<BorrowRecordResponseDTO> ongoingBorrowRecords(
            @RequestParam(name = "page", defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize

    ) {
        return borrowRecordService.findOngoingBorrows(pageNumber,pageSize);
    }

    @GetMapping("/history")
    public Page<BorrowRecordResponseDTO> borrowHistory(
            @RequestParam(name = "page", defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        return borrowRecordService.getBorrowHistory(pageNumber,pageSize);
    }

    @DeleteMapping("{Id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("Id") Integer id) {
        borrowRecordService.delete(id);
    }

    @PutMapping("{Id}/return")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> bookReturn(@PathVariable("Id") Integer id) {
        try {
            borrowRecordService.returnBook(id);
            return new ResponseEntity<>(null, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("{Id}/test-return")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> testBookReturn(@PathVariable("Id") Integer id,
                                                 @RequestParam(name="delay", defaultValue = "0") Integer delay ) {
        try {
            borrowRecordService.testReturnBook(id, delay);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // TODO: add fines for loosing book
    @PutMapping("{Id}/booklost")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Double> lost(
            @PathVariable("Id") Integer id) {
        try {
            borrowRecordService.bookLost(id);
            var fine = borrowRecordService.updateFine(id);
            return new ResponseEntity<>(fine, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("{Id}/fine")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Double> calculatefine(
            @PathVariable("Id") Integer id) {
        try {
            return new ResponseEntity<>(borrowRecordService.updateFine(id), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
