package com.eshan.library.controllers;

import com.eshan.library.models.LibrarianInfo;
import com.eshan.library.services.AdminService;
import com.eshan.library.services.EmailUpdateDTO;
import com.eshan.library.services.NameUpdateDTO;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.eshan.library.models.StudentInfo;
import com.eshan.library.services.StudentInfoService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/studentinfo")
@CrossOrigin
public class StudentInfoController {
    private final StudentInfoService studentInfoService;
    private final AdminService adminService;

    public StudentInfoController(StudentInfoService studentInfoService, AdminService adminService) {
        this.studentInfoService = studentInfoService;
        this.adminService = adminService;
    }

    @PostMapping
    public ResponseEntity<StudentInfo> post(@RequestBody StudentInfoDTO studentInfoDTO) {
        try {

            StudentInfo studentInfo = new StudentInfo();
            studentInfo.setName(studentInfoDTO.getName());
            studentInfo.setEmail(studentInfoDTO.getEmail());
            studentInfo.setId(studentInfoDTO.getId());

            var admin = adminService.findAdminById(studentInfoDTO.getAdminId());

            if (admin == null) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            studentInfo.setAdmin(admin);
            StudentInfo savedStudentInfo = studentInfoService.saveStudentInfo(studentInfo);
            return new ResponseEntity<StudentInfo>(savedStudentInfo, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("{studentId}")
    public StudentInfo findStudentInfoById(@PathVariable("studentId") Integer id) {
        return studentInfoService.findStudentInfoById(id);
    }

    @GetMapping
    public List<StudentInfo> findAllStudentInfo() {
        return studentInfoService.findAllStudentInfo();
    }

    @DeleteMapping("{studentId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteStudentInfo(@PathVariable("studentId") Integer id) {
        studentInfoService.deleteStudentInfo(id);
    }

    @PutMapping("email/{studentId}")
    @ResponseStatus(HttpStatus.OK)
    // public void updateStudentInfoEmail(@RequestBody StudentInfo
    // studentInfo,@PathVariable("studentId")Integer id){
    public ResponseEntity<StudentInfo> updateStudentInfoEmail(@RequestBody EmailUpdateDTO emailUpdateDTO,
            @PathVariable("studentId") Integer id) {
        try {
            studentInfoService.updateStudentInfoEmail(emailUpdateDTO, id);
            return new ResponseEntity<>(null, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("name/{studentId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<StudentInfo> updateStudentInfoName(@RequestBody NameUpdateDTO nameUpdateDTO,
            @PathVariable("studentId") Integer id) {
        try {
            studentInfoService.updateStudentInfoName(nameUpdateDTO, id);
            return new ResponseEntity<>(null, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @Data
    static class StudentInfoDTO {
        private Integer id;
        private String name;
        private String email;
        private Integer adminId;
    }
}
