package com.eshan.library.configs;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthenticationProvider authenticationProvider;

        @Bean
        CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration config = new CorsConfiguration();
                // React frontend
                config.setAllowedOrigins(
                                List.of("http://localhost:5173",
                                        "http://192.168.0.111:5173",
                                        "http://192.168.*.*:[*]"
                                // "http://localhost:5173/books",
                                // "http://localhost:5173/dashboard",
                                // "http://localhost:5173/borrow-records",
                                // "http://localhost:5173/requested-books",
                                // "http://localhost:5173/review-requests"
                                ));
                config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
                config.setAllowedHeaders(
                                List.of(HttpHeaders.CONTENT_TYPE, HttpHeaders.AUTHORIZATION));
                config.setAllowCredentials(true);
                UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
                src.registerCorsConfiguration("/**", config);
                return src;

        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
                httpSecurity
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()));
                httpSecurity.csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(requests -> requests
                                                .requestMatchers("/auth/**").permitAll()
                                                .requestMatchers("/student-auth/**").permitAll()
                                                .requestMatchers("/librarian-auth/**").permitAll()
                                                .requestMatchers("/students/**").hasAnyAuthority("STUDENT", "ADMIN")
                                                .requestMatchers(HttpMethod.GET, "/librarians")
                                                .hasAnyAuthority("STUDENT", "LIBRARIAN", "ADMIN")
                                                .requestMatchers("/librarians/**").hasAnyAuthority("LIBRARIAN", "ADMIN")

                                                .requestMatchers(HttpMethod.GET, "/books")
                                                .hasAnyAuthority("STUDENT", "LIBRARIAN", "ADMIN")

                                                .requestMatchers(HttpMethod.GET, "/books/**").hasAnyAuthority("STUDENT","LIBRARIAN", "ADMIN")

                                                .requestMatchers("/books/**").hasAnyAuthority("LIBRARIAN", "ADMIN")

                                                .requestMatchers(HttpMethod.PUT, "/bookrequests")
                                                .hasAnyAuthority("LIBRARIAN", "ADMIN")
                                                .requestMatchers("/bookrequests/**")
                                                .hasAnyAuthority("LIBRARIAN", "STUDENT", "ADMIN")
                                                .requestMatchers("/borrowrecords/**")
                                                .hasAnyAuthority("LIBRARIAN", "STUDENT", "ADMIN")
                                                .requestMatchers("/**").hasAuthority("ADMIN")
                                                .anyRequest().authenticated())
                                .sessionManagement(management -> management
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
                return httpSecurity.build();
        }
}
