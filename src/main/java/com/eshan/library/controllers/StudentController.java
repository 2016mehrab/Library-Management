package com.eshan.library.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.eshan.library.models.Student;
import com.eshan.library.services.StudentDTO;
import com.eshan.library.services.StudentService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/students")
@AllArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<String> post(@RequestBody StudentDTO studentDTO) {
        try {
            studentService.save(studentDTO);
            return new ResponseEntity<>(null, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("{Id}")

    public Student findById(@PathVariable("Id") Integer id) {
        return studentService.findStudentById(id);
    }

    @GetMapping
    public List<Student> findAll() {
        return studentService.findAll();
    }

    @DeleteMapping("{Id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLibrarian(@PathVariable("Id") Integer id) {
        studentService.delete(id);
    }

    @PutMapping("{Id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Student> updatePassword(@RequestBody Student student,
            @PathVariable("Id") Integer id) {
        try {
            studentService.updatePassword(student, id);
            return new ResponseEntity<>(null, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
