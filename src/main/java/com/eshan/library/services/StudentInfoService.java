package com.eshan.library.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eshan.library.models.StudentInfo;
import com.eshan.library.repositories.StudentInfoRepository;

@Service
public class StudentInfoService {
    private final StudentInfoRepository studentInfoRepository;

    public StudentInfoService(StudentInfoRepository studentInfoRepository) {
        this.studentInfoRepository = studentInfoRepository;
    }
    public StudentInfo saveStudentInfo(StudentInfo studentInfo){
        return  studentInfoRepository.save(studentInfo);
    }
    public StudentInfo findStudentInfoById(Integer id){
        return studentInfoRepository.findById(id).orElse(null);
    }
    public List<StudentInfo> findAllStudentInfo(){
        return studentInfoRepository.findAll();
    }
    public void deleteStudentInfo(Integer id){
        studentInfoRepository.deleteById(id);
    }
    public void updateStudentInfoEmail(StudentInfo studentInfo, Integer id){
        StudentInfo updatedStudentInfo = studentInfoRepository.findById(id).orElse(null);
        if(studentInfo!=null){
            updatedStudentInfo.setEmail(studentInfo.getEmail());
            studentInfoRepository.save(updatedStudentInfo);
        }
    }
    public void updateStudentInfoName(StudentInfo studentInfo, Integer id){
        StudentInfo updatedStudentInfo = studentInfoRepository.findById(id).orElse(null);
        if(studentInfo!=null){
            updatedStudentInfo.setName(studentInfo.getName());
            studentInfoRepository.save(updatedStudentInfo);
        }
    }
    

}
