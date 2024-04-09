package com.eshan.library.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.eshan.library.models.StudentInfo;
import com.eshan.library.services.StudentInfoService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/studentinfo")
public class StudentInfoController {
    private final StudentInfoService studentInfoService;

    public StudentInfoController(StudentInfoService studentInfoService) {
        this.studentInfoService = studentInfoService;
    }
    
    @PostMapping
    public StudentInfo post(@RequestBody StudentInfo studentInfo){
        return studentInfoService.saveStudentInfo(studentInfo);
    }
    @GetMapping("{studentId}")
    public StudentInfo findStudentInfoById(@PathVariable("studentId")Integer id){
        return studentInfoService.findStudentInfoById(id);
    }
    @GetMapping
    public List<StudentInfo> findAllStudentInfo(){return studentInfoService.findAllStudentInfo();}
    
    @DeleteMapping("{studentId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteStudentInfo(@PathVariable("studentId")Integer id){
        studentInfoService.deleteStudentInfo(id);
    }

    @PutMapping("{studentId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateStudentInfoEmail(@RequestBody StudentInfo studentInfo,@PathVariable("studentId")Integer id){
        studentInfoService.updateStudentInfoEmail(studentInfo, id);
    }

    @PutMapping("{studentId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateStudentInfoName(@RequestBody StudentInfo studentInfo,@PathVariable("studentId")Integer id){
        studentInfoService.updateStudentInfoName(studentInfo, id);
    }
}
