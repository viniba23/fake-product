package org.example.fake.config;

import java.util.Collections;

import org.example.fake.model.User;
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
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                		"/",
                        "/user/login",
                        "/admin/login",
                        "/user/register",
                        "/user/forgot-password",
                        "/user/verify-otp",
                        "/user/reset-password",
                        "/admin/forgot-password",
                        "/admin/verify-otp",
                        "/admin/reset-password",
                        "/css/**",
                        "/js/**"
                ).permitAll()
                .requestMatchers("/user/dashboard").hasAnyRole("USER","ADMIN")
                .requestMatchers("/admin/**").permitAll()
                .requestMatchers("/user/forgot-password").permitAll()

                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/user/login")
                .loginProcessingUrl("/user/login") // This should match your form action
                .defaultSuccessUrl("/user/dashboard")
                .failureUrl("/user/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/user/login?logout")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                )
            .sessionManagement(session -> session
                    .maximumSessions(1)
                    .maxSessionsPreventsLogin(false)
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
//            if (user == null) {
//                throw new UsernameNotFoundException("User not found");
//            }
//            if (!user.isActive()) {
//                throw new UsernameNotFoundException("User is not activated by admin");
//            }
//            return new org.springframework.security.core.userdetails.User(
//                user.getEmail(),
//                user.getPassword(),
//                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
//            );
//        };
//    }
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> {
            User user = userRepository.findByEmail(username);

            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }

            else if (!user.isActive()) {
                throw new UsernameNotFoundException("Account not activated by admin");
            }

            return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
            );
        };
    }

}
