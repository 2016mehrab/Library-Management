package com.eshan.library.services;

import com.eshan.library.models.Admin;
import com.eshan.library.repositories.AdminRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public Admin saveAdmin(Admin adminObj){
        return adminRepository.save(adminObj);
    }
    public Admin findAdminById(Integer id){
        return adminRepository.findById(id).orElse(null);
    }
    public List<Admin> findAllAdmins(){
        return adminRepository.findAll();
    }
    public void deleteAdmin(Integer id){
        adminRepository.deleteById(id);
    }
    public void updateAdminPassword(Admin admin,Integer id){
        Admin updatedAdmin = adminRepository.findById(id).orElse(null);
        if(admin !=null){
            updatedAdmin.setPassword(admin.getPassword());
            adminRepository.save(updatedAdmin);
        }
    }

}
