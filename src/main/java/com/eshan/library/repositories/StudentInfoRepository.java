package com.eshan.library.repositories;

import com.eshan.library.models.StudentInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentInfoRepository extends JpaRepository<StudentInfo,Integer> {
}
