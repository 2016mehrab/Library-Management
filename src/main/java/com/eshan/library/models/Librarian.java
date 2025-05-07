package com.eshan.library.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Librarian implements UserDetails {
        @Id
        private Integer profileId;

        // @PrimaryKeyJoinColumn
        @OneToOne
        @MapsId
        @JsonManagedReference("librarian-librarianInfo")
        private LibrarianInfo librarianInfo;

        @Column(unique = true, nullable = false)
        private String email;

        @Column(unique = true, nullable = false)
        private String username;
        private String password;
        // @OneToMany(mappedBy = "librarian", cascade = CascadeType.ALL, orphanRemoval =
        // true)
        @OneToMany(mappedBy = "librarian")
        @JsonManagedReference("bookrequest-librarian")
        private List<BookRequest> bookRequests;

        // @OneToMany(mappedBy = "librarian", cascade = CascadeType.ALL, orphanRemoval =
        // true)
        @OneToMany(mappedBy = "librarian")
        @JsonManagedReference("librarian-borrowrecord")
        private List<BorrowRecord> borrowRecords;

        @Column(unique = true, name="reset_token")
        private String resetToken;

        @Column(name = "reset_token_expiry")
        private LocalDateTime resetTokenExpiry;

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
                return "Librarian{id=" + profileId + ", username='" + username + "', role=" + role + "}";
        }
}
