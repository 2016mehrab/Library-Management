package com.eshan.library.configs;

import javax.management.relation.Role;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthenticationProvider studentAuthenticationProvider;
        private final AuthenticationProvider librarianAuthenticationProvider;
        private final AuthenticationProvider adminAuthenticationProvider;

        @Bean
        public AuthenticationProvider delegatingAuthenticationProvider() {
                return new DelegatingAuthenticationProvider(studentAuthenticationProvider,
                                librarianAuthenticationProvider, adminAuthenticationProvider);
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
                httpSecurity.csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(requests -> requests
                                                .requestMatchers("/auth/**").permitAll()
                                                .requestMatchers("/student-auth/**").permitAll()
                                                // .requestMatchers("/student-auth/**").permitAll()
                                                .requestMatchers("/students/**").hasAnyAuthority("STUDENT", "ADMIN")
                                                .requestMatchers("/librarian/**").hasAnyAuthority("LIBRARIAN", "ADMIN")
                                                .requestMatchers("/**").hasAuthority("ADMIN")
                                                .anyRequest().authenticated())
                                .sessionManagement(management -> management
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authenticationProvider(delegatingAuthenticationProvider())
                                // .authenticationProvider(librarianAuthenticationProvider)
                                // .authenticationProvider(adminAuthenticationProvider)
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
                return httpSecurity.build();
        }
}
