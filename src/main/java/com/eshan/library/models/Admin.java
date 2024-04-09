package com.eshan.library.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Entity
@Builder
public class Admin {
    @Id
    @GeneratedValue
    @Column(
            name = "admin_id"
    )
    private Integer id;
    @Column(
            unique = true,
            nullable = false
    )
    private String username;
    private String password;
    @OneToMany(mappedBy = "admin")
    private List<LibrarianInfo> librarianInfoList;
    @OneToMany(mappedBy = "admin")
    @JsonManagedReference
    private List<StudentInfo> studentInfoList;

}
