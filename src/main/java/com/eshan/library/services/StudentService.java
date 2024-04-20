package com.eshan.library.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eshan.library.models.Role;
import com.eshan.library.models.Student;
import com.eshan.library.models.StudentInfo;
import com.eshan.library.repositories.StudentInfoRepository;
import com.eshan.library.repositories.StudentRepository;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentInfoRepository studentInfoRepository;

    public Student save(StudentDTO studentDTO) {
        var student = toStudent(studentDTO);
        return studentRepository.save(student);
    }

    private Student toStudent(StudentDTO studentDTO) {
        var student =  new Student();

        student.setUsername(studentDTO.username());
        student.setPassword(studentDTO.password());
        student.setRole(Role.STUDENT);


        StudentInfo studentInfo = studentInfoRepository.findById(studentDTO.studentId()).orElse(null);
        if (studentInfo != null) {
            student.setStudentInfo(studentInfo);
            return student;
        } else {
            throw new RuntimeException("Operation was not successful: StudentInfo not found");

        }
    }

// TODO: create proper response dto
    public Student findStudentById(Integer id) {
        StudentInfo studentInfo = studentInfoRepository.findById(id).orElse(null);

        // return studentInfo.getStudent();
        return studentRepository.findById(studentInfo.getStudent().getId()).orElse(null);
    }

// TODO: create proper response dto
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public void delete(Integer id) {
        StudentInfo studentInfo = studentInfoRepository.findById(id).orElse(null);
        studentRepository.deleteById(studentInfo.getStudent().getId());
        // studentRepository.deleteById();
    }

    public void updatePassword(Student student, Integer id) {
        Student updatedS = studentRepository.findById(id).orElse(null);
        if (updatedS != null) {
            updatedS.setPassword(student.getPassword());
            studentRepository.save(updatedS);
        } else {
            throw new RuntimeException("Operation was not successful: Student Not Found!");
        }
    }


}
