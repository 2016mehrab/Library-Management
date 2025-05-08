package com.eshan.library.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eshan.library.models.Admin;
import com.eshan.library.models.StudentInfo;
import com.eshan.library.repositories.AdminRepository;
import com.eshan.library.repositories.StudentInfoRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentInfoService {
    private final StudentInfoRepository studentInfoRepository;
    private final AdminRepository adminRepository;

    public StudentInfoService(StudentInfoRepository studentInfoRepository, AdminRepository adminRepository) {
        this.studentInfoRepository = studentInfoRepository;
        this.adminRepository = adminRepository;
    }

    @Transactional
    public StudentInfo saveStudentInfo(StudentInfo studentInfo) {
        Admin admin = adminRepository.findById(studentInfo.getAdmin().getId()).orElse(null);
        if (studentInfoRepository.existsById(studentInfo.getId())) {
            throw new RuntimeException("Operation was not successful: Student exists");
        }
        if (admin != null) {
            studentInfo.setAdmin(admin);
            return studentInfoRepository.save(studentInfo);
        } else {
            throw new RuntimeException("Operation was not successful: Admin not found");
        }
    }

    @Transactional(readOnly = true)
    public StudentInfo findStudentInfoById(Integer id) {
        return studentInfoRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<StudentInfo> findAllStudentInfo() {
        return studentInfoRepository.findAll();
    }

    @Transactional
    public void deleteStudentInfo(Integer id) {
        studentInfoRepository.deleteById(id);
    }

    @Transactional
    public void updateStudentInfoEmail(EmailUpdateDTO emailUpdateDTO, Integer id) {
        StudentInfo updatedStudentInfo = studentInfoRepository.findById(id).orElse(null);
        if (emailUpdateDTO.email() != null && updatedStudentInfo != null) {
            updatedStudentInfo.setEmail(emailUpdateDTO.email());
            studentInfoRepository.save(updatedStudentInfo);
        } else {
            throw new RuntimeException("Operation was not successful: Student Not Found!");
        }
    }

    @Transactional
    public void updateStudentInfoName(NameUpdateDTO nameUpdateDTO, Integer id) {
        StudentInfo updatedStudentInfo = studentInfoRepository.findById(id).orElse(null);
        if (nameUpdateDTO.name() != null && updatedStudentInfo!=null) {
            updatedStudentInfo.setName(nameUpdateDTO.name());
            studentInfoRepository.save(updatedStudentInfo);
        } else {
            throw new RuntimeException("Operation was not successful: Student Not Found!");
        }
    }

}
