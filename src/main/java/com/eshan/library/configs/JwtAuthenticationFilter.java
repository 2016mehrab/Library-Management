package com.eshan.library.configs;

import java.io.IOException;

import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService adminDetailsService;
    private final UserDetailsService studentDetailsService;
    private final UserDetailsService librarianDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;
        final String role;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        username = jwtService.extractUsername(jwt);
        role = jwtService.extractRole(jwt);
        if (role.equals("ADMIN")  && username != null
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.adminDetailsService.loadUserByUsername(username);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        else if (role.equals("STUDENT")  && username != null
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.studentDetailsService.loadUserByUsername(username);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        else if (role.equals("LIBRARIAN")  && username != null
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.librarianDetailsService.loadUserByUsername(username);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // if (request.getRequestURI().startsWith("/admins") && username != null
        //         && SecurityContextHolder.getContext().getAuthentication() == null) {
        //     UserDetails userDetails = this.adminDetailsService.loadUserByUsername(username);
        //     if (jwtService.isTokenValid(jwt, userDetails)) {
        //         UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
        //                 null, userDetails.getAuthorities());
        //         authToken.setDetails(new WebAuthenticationDetailsSource()
        //                 .buildDetails(request));
        //         SecurityContextHolder.getContext().setAuthentication(authToken);
        //     }
        // }
        filterChain.doFilter(request, response);
    }
}
