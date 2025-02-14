package com.eshan.library.models;

import jakarta.persistence.*;
import lombok.*;

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
public class Admin implements UserDetails {
        @Id
        @GeneratedValue
        @Column(name = "admin_id")
        private Integer id;
        @Column(unique = true, nullable = false)
        private String username;
        private String password;
        @OneToMany(mappedBy = "admin")
        private List<LibrarianInfo> librarianInfoList;
        @OneToMany(mappedBy = "admin")
        @JsonManagedReference
        private List<StudentInfo> studentInfoList;
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
                return "Admin{id=" + id + ", username='" + username + "', role=" + role + "}";
        }

}
