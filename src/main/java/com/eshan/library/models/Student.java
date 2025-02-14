package com.eshan.library.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Entity
@Builder
public class Student implements UserDetails{
        @Id
        @Column(name = "student_id")
        private Integer id;
        @OneToOne
        @MapsId
        @JoinColumn(name = "student_id", referencedColumnName = "id")
        @JsonManagedReference("student-studentInfo")
        private StudentInfo studentInfo;
        @Column(unique = true, nullable = false)
        private String username;
        private String password;
        @OneToMany(mappedBy = "student")
        @JsonManagedReference("student-borrowrecord")
        private List<BorrowRecord> borrowRecords;

        @OneToMany(mappedBy = "student")
        @JsonManagedReference("bookrequest-student")
        private List<BookRequest> bookRequests;

        @Enumerated(EnumType.STRING)
        private Role role;

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
                
                return List.of(new SimpleGrantedAuthority(role.name()));
        }

        @Override
        public boolean isAccountNonExpired() {
                return true;
        }

        @Override
        public boolean isAccountNonLocked() {
                return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
                return true;
        }

        @Override
        public boolean isEnabled() {
                return true;
        }

        @Override
        public String toString() {
                return "Student{id=" + id + ", username='" + username + "', role=" + role + "}";
        }
}
