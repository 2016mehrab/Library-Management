package com.eshan.library.configs;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import jakarta.servlet.http.HttpServletRequest;


public class DelegatingAuthenticationProvider implements AuthenticationProvider {

    private final AuthenticationProvider studentAuthenticationProvider;
    private final AuthenticationProvider librarianAuthenticationProvider;
    private final AuthenticationProvider adminAuthenticationProvider;

    public DelegatingAuthenticationProvider(AuthenticationProvider studentAuthenticationProvider,
                                            AuthenticationProvider librarianAuthenticationProvider,
                                            AuthenticationProvider adminAuthenticationProvider) {
        this.studentAuthenticationProvider = studentAuthenticationProvider;
        this.librarianAuthenticationProvider = librarianAuthenticationProvider;
        this.adminAuthenticationProvider = adminAuthenticationProvider;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        HttpServletRequest request = ((FilterInvocation) authentication.getDetails()).getRequest();
        String requestURL = request.getRequestURL().toString();

        if (requestURL.contains("/student-auth")) {
            return studentAuthenticationProvider.authenticate(authentication);
        } else if (requestURL.contains("/librarian-auth")) {
            return librarianAuthenticationProvider.authenticate(authentication);
        } else {
            return adminAuthenticationProvider.authenticate(authentication);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return studentAuthenticationProvider.supports(authentication) &&
               librarianAuthenticationProvider.supports(authentication) &&
               adminAuthenticationProvider.supports(authentication);
    }
}

