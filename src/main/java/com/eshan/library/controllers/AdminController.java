package com.eshan.library.controllers;

import com.eshan.library.models.Admin;
import com.eshan.library.services.AdminService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/admins")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping
    public Admin post(@RequestBody Admin admin){
        return adminService.saveAdmin(admin);

    }
    @GetMapping("{adminId}")
    public Admin findAdminById(@PathVariable("adminId")Integer id){
       return adminService.findAdminById(id) ;
    }
    @GetMapping
    public  List<Admin> findAllAdmins(){
        return adminService.findAllAdmins();
    }
    @DeleteMapping("{adminId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAdmin(
            @PathVariable("adminId")Integer id
    )
    {
        adminService.deleteAdmin(id);
    }
    @PutMapping("{adminId}")
    @ResponseStatus(HttpStatus.OK)
    public  void updatePassword(
            @RequestBody Admin admin,
            @PathVariable ("adminId") Integer id
    ){
        adminService.updateAdminPassword(admin,id);

    }


}

