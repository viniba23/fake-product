package org.example.fake.service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.example.fake.model.PasswordResetToken;
import org.example.fake.model.User;
import org.example.fake.repo.PasswordResetTokenRepository;
import org.example.fake.repo.UserRepository;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private PasswordResetTokenRepository tokenRepository;
    
    @Autowired
    private EmailService emailService;
    
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public User registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

   

    @Transactional
    public void initiatePasswordReset(String email) {
        System.out.println("[DEBUG] Starting password reset for email: " + email);
        
        try {
            // Step 1: Find user by email
            System.out.println("[DEBUG] Looking up user with email: " + email);
            User user = userRepository.findByEmail(email);
            
            if (user == null) {
                System.out.println("[DEBUG] User not found for email: " + email);
                throw new RuntimeException("Email not found");
            }
            System.out.println("[DEBUG] Found user: " + user.getUsername());
            
            // Step 2: Delete existing tokens
            System.out.println("[DEBUG] Deleting existing tokens for email: " + email);
            tokenRepository.deleteByEmail(email);
            System.out.println("[DEBUG] Successfully deleted old tokens");
            
            // Step 3: Generate new OTP
            String otp = String.format("%06d", new Random().nextInt(999999));
            System.out.println("[DEBUG] Generated OTP: " + otp);
            
            // Step 4: Create and save new token
            PasswordResetToken resetToken = new PasswordResetToken();
            resetToken.setToken(otp);
            resetToken.setEmail(email);
            resetToken.setUser(user);
            resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));
            
            System.out.println("[DEBUG] Saving new token to database");
            tokenRepository.save(resetToken);
            System.out.println("[DEBUG] Token saved successfully");
            
            // Step 5: Send email
            System.out.println("[DEBUG] Attempting to send email to: " + email);
            emailService.sendPasswordResetEmail(email, otp);
            System.out.println("[DEBUG] Email sent successfully");
            
        } catch (Exception e) {
            System.out.println("[ERROR] Exception in initiatePasswordReset: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public boolean validateResetToken(String token, String email) {
        PasswordResetToken resetToken = tokenRepository.findByTokenAndEmail(token, email);
        return resetToken != null && resetToken.getExpiryDate().isAfter(LocalDateTime.now());
    }

    public void resetPassword(String token, String email, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByTokenAndEmail(token, email);
        if (resetToken != null && resetToken.getExpiryDate().isAfter(LocalDateTime.now())) {
            User user = resetToken.getUser();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            tokenRepository.delete(resetToken);
        }
    }

    public User authenticate(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }
}
