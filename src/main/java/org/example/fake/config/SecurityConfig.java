package org.example.fake.config;

import java.util.Collections;

import org.example.fake.model.Admin;
import org.example.fake.model.User;
import org.example.fake.repo.AdminRepository;
import org.example.fake.repo.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//import org.example.fake.model.User;
//import org.example.fake.repo.UserRepository;

@Configuration
public class SecurityConfig {


	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            // 🚨 CRITICAL FIX
            .authorizeHttpRequests(auth -> auth

                // ✅ FULL ADMIN MODULE FREE ACCESS
                .requestMatchers("/admin/**").permitAll()

                // ✅ PUBLIC USER PAGES
                .requestMatchers(
                        "/",
                        "/user/login",
                        "/user/register",
                        "/user/forgot-password",
                        "/user/verify-otp",
                        "/user/reset-password",
                        "/css/**",
                        "/js/**"
                ).permitAll()

                // 🔐 USER DASHBOARD PROTECTED
//                .requestMatchers("/user/dashboard").authenticated()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/user/**").hasRole("USER")

                .anyRequest().authenticated()
            )

            // USER LOGIN (ONLY USER)
            .formLogin(form -> form
                .loginPage("/user/login")
                .loginProcessingUrl("/user/login")
                .defaultSuccessUrl("/user/dashboard", true)
                .failureUrl("/user/login?error=true")
            )

            .logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public UserDetailsService userDetailsService(UserRepository userRepository) {
//        return username -> {
//            User user = userRepository.findByEmail(username);
//
//            if (user == null) {
//                throw new UsernameNotFoundException("User not found");
//            }
//
//            else if (!user.isActive()) {
//                throw new UsernameNotFoundException("Account not activated by admin");
//            }
//
//            return new org.springframework.security.core.userdetails.User(
//                user.getEmail(),
//                user.getPassword(),
//                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
//            );
//        };
//    }
    
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository,
                                                 AdminRepository adminRepository) {

        return username -> {

            // First check Admin
            Admin admin = adminRepository.findByEmail(username);
            if (admin != null) {
                return new org.springframework.security.core.userdetails.User(
                        admin.getEmail(),
                        admin.getPassword(),
                        Collections.singletonList(
                                new SimpleGrantedAuthority("ROLE_ADMIN")
                        )
                );
            }

            // Then check User
            User user = userRepository.findByEmail(username);
            if (user != null && user.isActive()) {
                return new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        Collections.singletonList(
                                new SimpleGrantedAuthority("ROLE_USER")
                        )
                );
            }

            throw new UsernameNotFoundException("User not found");
        };
    }


}
