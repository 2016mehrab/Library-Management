package com.eshan.library.auths;

import com.eshan.library.models.Librarian;
import com.eshan.library.models.Student;
import com.eshan.library.repositories.LibrarianRepository;
import com.eshan.library.repositories.StudentRepository;
import com.postmarkapp.postmark.client.ApiClient;
import com.postmarkapp.postmark.client.data.model.message.Message;
import com.postmarkapp.postmark.client.exception.PostmarkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {
    private  static  final Logger logger = LoggerFactory.getLogger(PasswordResetService.class);
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final LibrarianRepository librarianRepository;
    private final ApiClient postmarkApiClient;

    @Value("${app.api.url}")
    private String apiUrl;
    @Value("${server.port}")
    private String port;
    String getApiUrl(){
        return apiUrl+":"+port;
    }
    @Value("${postmark.from-email}")
    private String fromEmail;

    public PasswordResetService(StudentRepository studentRepository, PasswordEncoder passwordEncoder,
                                LibrarianRepository librarianRepository, ApiClient postmarkApiClient) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.librarianRepository = librarianRepository;
        this.postmarkApiClient = postmarkApiClient;
    }

    @Transactional
    public void sendPasswordResetEmail(String email) {
        UserDetails user = findUserByEmail(email);
        String token = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(30);
        if( user instanceof Student student){
           student.setResetToken(token);
           student.setResetTokenExpiry(expiry);
           studentRepository.save(student);
        }
        else if( user instanceof Librarian librarian){
            librarian.setResetToken(token);
            librarian.setResetTokenExpiry(expiry);
            librarianRepository.save(librarian);
        }
        else throw new RuntimeException("User not found");
        String sbj= "LibraryApp Password Reset";
        String msg= """
                Use this token to reset your password: %s \n
                Submit it to: POST %s/auth/reset-password \n
                Example request body: {"token":"%s", "newPassword":"yourNewPassword"} \n
                Token expires at: %s \n
                """.formatted(token, getApiUrl(), token ,expiry );
        Message message = new Message(
               fromEmail, email,sbj,msg
        );
        try{
            postmarkApiClient.deliverMessage(message);
            logger.info("Password reset email sent to {}", email);
        } catch (PostmarkException e) {
            logger.error("Postmark error sending email to {}: {}", email, e.getMessage());
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        } catch (IOException e) {
            logger.error("error sending email to {}: {}", email, e.getMessage());
            throw new RuntimeException(e);
        }
    }
    @Transactional
    public void resetPassword(String token ,String newPassword) {
        var studentOpt = studentRepository.findByResetToken(token);
        if(studentOpt.isPresent() && studentOpt.get().getResetTokenExpiry()!=null && studentOpt.get().getResetTokenExpiry().isAfter(LocalDateTime.now())){
            var student = studentOpt.get();
            student.setPassword(passwordEncoder.encode(newPassword));
            student.setResetToken(null);
            student.setResetTokenExpiry(null);
            studentRepository.save(student);
            return ;
        }
        var librarianOpt = librarianRepository.findByResetToken(token);
        if(librarianOpt.isPresent() && librarianOpt.get().getResetTokenExpiry()!=null && librarianOpt.get().getResetTokenExpiry().isAfter(LocalDateTime.now())){
            var librarian = librarianOpt.get();
            librarian.setPassword(passwordEncoder.encode(newPassword));
            librarian.setResetToken(null);
            librarian.setResetTokenExpiry(null);
            librarianRepository.save(librarian);
            return;
        }
        throw new RuntimeException("Invalid or expired token");

    }

    private UserDetails findUserByEmail(String email) {
        return studentRepository.findByEmail(email).map(UserDetails.class::cast).orElseGet(()-> librarianRepository.findByEmail(email).map(UserDetails.class::cast).orElseThrow(()-> new RuntimeException("User not found")));
    }

}
