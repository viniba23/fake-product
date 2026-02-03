package org.example.fake.service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.example.fake.model.Admin;
import org.example.fake.model.PasswordResetToken;
import org.example.fake.repo.AdminRepository;
import org.example.fake.repo.PasswordResetTokenRepository;

import jakarta.annotation.PostConstruct;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PasswordResetTokenRepository tokenRepository;
    
    @Autowired
    private EmailService emailService;

    @PostConstruct
    public void createDefaultAdmin() {
        if (adminRepository.count() == 0) {
            Admin admin = new Admin();
            admin.setEmail("vinibca24@gmail.com");
            admin.setPassword(passwordEncoder.encode("admin"));
            adminRepository.save(admin);
        }
    }

    public Admin authenticate(String email, String password) {
        Admin admin = adminRepository.findByEmail(email);
        if (admin != null && passwordEncoder.matches(password, admin.getPassword())) {
            return admin;
        }
        return null;
    }
    
    @Transactional
    public void initiateAdminPasswordReset(String email) {
        Admin admin = adminRepository.findByEmail(email);
        if (admin != null) {
            // Clear existing tokens first
            tokenRepository.deleteByEmail(email);
            
            // Create new token
            String otp = String.format("%06d", new Random().nextInt(999999));
            
            PasswordResetToken resetToken = new PasswordResetToken();
            resetToken.setToken(otp);
            resetToken.setEmail(email);
            resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));
            
            tokenRepository.save(resetToken);
            emailService.sendPasswordResetEmail(email, otp);
        } else {
            throw new RuntimeException("Admin email not found");
        }
    }
   

    public boolean validateAdminResetToken(String token, String email) {
        PasswordResetToken resetToken = tokenRepository.findByTokenAndEmail(token, email);
        return resetToken != null && resetToken.getExpiryDate().isAfter(LocalDateTime.now());
    }

    public void resetAdminPassword(String token, String email, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByTokenAndEmail(token, email);
        if (resetToken != null && resetToken.getExpiryDate().isAfter(LocalDateTime.now())) {
            Admin admin = adminRepository.findByEmail(email);
            admin.setPassword(passwordEncoder.encode(newPassword));
            adminRepository.save(admin);
            tokenRepository.delete(resetToken);
        }
    }
}
