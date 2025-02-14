package com.eshan.library.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
public class Librarian implements UserDetails {
        @Id
        @GeneratedValue
        @Column(unique = true, nullable = false)
        private Integer id;
        @Column(unique = true, nullable = false)
        private String username;
        private String password;
        @OneToMany(mappedBy = "librarian", cascade = CascadeType.ALL, orphanRemoval = true)
        @JsonManagedReference("bookrequest-librarian")
        private List<BookRequest> bookRequests;
        @OneToOne
        @JoinColumn(name = "librarian_id")
        @JsonManagedReference("librarian-librarianInfo")
        private LibrarianInfo librarianInfo;

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
                return "Librarian{id=" + id + ", username='" + username + "', role=" + role + "}";
        }
}
