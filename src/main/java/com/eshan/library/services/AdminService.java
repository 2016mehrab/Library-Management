package com.eshan.library.services;

import com.eshan.library.models.Admin;
import com.eshan.library.repositories.AdminRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class AdminService {
    private AdminRepository adminRepository;
    public void saveAdmin(Admin adminObj){
        adminRepository.save(adminObj);
    }

}
