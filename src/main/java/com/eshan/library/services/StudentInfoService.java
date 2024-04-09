package com.eshan.library.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eshan.library.models.Admin;
import com.eshan.library.models.StudentInfo;
import com.eshan.library.repositories.AdminRepository;
import com.eshan.library.repositories.StudentInfoRepository;

@Service
public class StudentInfoService {
    private final StudentInfoRepository studentInfoRepository;
    private final AdminRepository adminRepository;

    public StudentInfoService(StudentInfoRepository studentInfoRepository, AdminRepository adminRepository) {
        this.studentInfoRepository = studentInfoRepository;
        this.adminRepository = adminRepository;
    }

    public StudentInfo saveStudentInfo(StudentInfo studentInfo) {
        Admin admin = adminRepository.findById(studentInfo.getAdmin().getId()).orElse(null);
        if(studentInfoRepository.existsById(studentInfo.getId())){
            throw new RuntimeException("Operation was not successful: Student exists");
        }
        if (admin != null) {
            studentInfo.setAdmin(admin);
            return studentInfoRepository.save(studentInfo);
        } else {
            throw new RuntimeException("Operation was not successful: Admin not found");
        }
    }

    public StudentInfo findStudentInfoById(Integer id) {
        return studentInfoRepository.findById(id).orElse(null);
    }

    public List<StudentInfo> findAllStudentInfo() {
        return studentInfoRepository.findAll();
    }

    public void deleteStudentInfo(Integer id) {
        studentInfoRepository.deleteById(id);
    }

    public void updateStudentInfoEmail(StudentInfo studentInfo, Integer id) {
        StudentInfo updatedStudentInfo = studentInfoRepository.findById(id).orElse(null);
        if (studentInfo != null) {
            updatedStudentInfo.setEmail(studentInfo.getEmail());
            studentInfoRepository.save(updatedStudentInfo);
        }
    }

    public void updateStudentInfoName(StudentInfo studentInfo, Integer id) {
        StudentInfo updatedStudentInfo = studentInfoRepository.findById(id).orElse(null);
        if (studentInfo != null) {
            updatedStudentInfo.setName(studentInfo.getName());
            studentInfoRepository.save(updatedStudentInfo);
        }
    }

}
