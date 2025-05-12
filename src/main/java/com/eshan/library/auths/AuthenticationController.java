package com.eshan.library.auths;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authService;
    private final PasswordResetService passwordResetService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request) {
         AuthenticationResponse res= authService.register(request);
        try {
            return new ResponseEntity<>(res, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @GetMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
       try{
          passwordResetService.sendPasswordResetEmail(email);
          return new ResponseEntity<>("Reset email sent", HttpStatus.OK);
       }
       catch(Exception e){
           return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
       }
    }
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetRequest rr) {
        try{
            passwordResetService.resetPassword(rr.getToken(), rr.getNewPassword());
            return new ResponseEntity<>("Password updated successfully", HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Data
    static class ResetRequest {
        private String token;
        private String newPassword;
    }

}
