package com.eshan.library.configs;


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
        public WebMvcConfigurer corsConfig(){
                return new WebMvcConfigurer() {
                        public void addCorsMappings(CorsRegistry registry){
                                WebMvcConfigurer.super.addCorsMappings(registry);
                                registry.addMapping("/**")
                               .allowedOrigins("http://localhost:5173")
                               .allowedMethods(HttpMethod.GET.name(),
                               HttpMethod.POST.name(),
                               HttpMethod.PUT.name(),
                               HttpMethod.DELETE.name()
                               ).allowedHeaders(
                                HttpHeaders.CONTENT_TYPE,
                                HttpHeaders.AUTHORIZATION
                               );
                                
                        }
                };
        }
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
                httpSecurity.csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(requests -> requests
                                                .requestMatchers("/auth/**").permitAll()
                                                .requestMatchers("/student-auth/**").permitAll()
                                                .requestMatchers("/librarian-auth/**").permitAll()
                                                .requestMatchers("/students/**").hasAnyAuthority("STUDENT", "ADMIN")
                                                .requestMatchers(HttpMethod.GET, "/librarians").hasAnyAuthority("STUDENT", "LIBRARIAN", "ADMIN")
                                                .requestMatchers("/librarians/**").hasAnyAuthority("LIBRARIAN", "ADMIN")
                                                .requestMatchers(HttpMethod.GET, "/books").hasAnyAuthority("STUDENT", "LIBRARIAN", "ADMIN")
                                                .requestMatchers("/books/**").hasAnyAuthority("LIBRARIAN", "ADMIN")
                                                .requestMatchers(HttpMethod.PUT, "/bookrequests").hasAnyAuthority( "LIBRARIAN", "ADMIN")
                                                .requestMatchers("/bookrequests/**").hasAnyAuthority("LIBRARIAN", "STUDENT","ADMIN")
                                                .requestMatchers("/borrowrecords/**").hasAnyAuthority("LIBRARIAN", "STUDENT","ADMIN")
                                                .requestMatchers("/**").hasAuthority("ADMIN")
                                                .anyRequest().authenticated())
                                .sessionManagement(management -> management
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
                return httpSecurity.build();
        }
}
