//package org.example.fake.config;
//
//import java.util.Collections;
//
//import org.example.fake.model.Admin;
//import org.example.fake.model.User;
//import org.example.fake.repo.AdminRepository;
//import org.example.fake.repo.UserRepository;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
////import org.example.fake.model.User;
////import org.example.fake.repo.UserRepository;
//
//@Configuration
//public class SecurityConfig {
//
//
//	@Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//        http
//            .csrf(csrf -> csrf.disable())
//
////            // 🚨 CRITICAL FIX
////            .authorizeHttpRequests(auth -> auth
////
////                // ✅ FULL ADMIN MODULE FREE ACCESS
////                .requestMatchers("/admin/**").permitAll()
////
////                // ✅ PUBLIC USER PAGES
////                .requestMatchers(
////                        "/",
////                        "/images/**",
////                        "/user/login",
////                        "/user/register",
////                        "/user/forgot-password",
////                        "/user/verify-otp",
////                        "/user/reset-password",
////                        "/css/**",
////                        "/js/**"
////                ).permitAll()
////
////                // 🔐 USER DASHBOARD PROTECTED
//////                .requestMatchers("/user/dashboard").authenticated()
////                .requestMatchers("/admin/**").hasRole("ADMIN")
////                .requestMatchers("/user/**").hasRole("USER")
////
////                .anyRequest().authenticated()
////            )
//            .authorizeHttpRequests(auth -> auth
//
//                    .requestMatchers(
//                            "/",
//                            "/images/**",
//                            "/css/**",
//                            "/js/**"
//                    ).permitAll()
//
//                    .requestMatchers(
//                            "/user/login",
//                            "/user/register",
//                            "/user/forgot-password",
//                            "/user/verify-otp",
//                            "/user/reset-password"
//                    ).permitAll()
//
//                    .requestMatchers(
//                            "/admin/login",
//                            "/admin/forgot-password",
//                            "/admin/reset-password"
//                    ).permitAll()
//
//                    .requestMatchers("/admin/**").hasRole("ADMIN")
//                    .requestMatchers("/user/**").hasRole("USER")
//
//                    .anyRequest().authenticated()
//            )
//            // USER LOGIN (ONLY USER)
//            .formLogin(form -> form
//                    .loginPage("/user/login")
//                    .loginProcessingUrl("/login")
//                    .successHandler((request, response, authentication) -> {
//
//                        boolean isAdmin = authentication.getAuthorities()
//                                .stream()
//                                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
//
//                        if (isAdmin) {
//                            response.sendRedirect("/admin/dashboard");
//                        } else {
//                            response.sendRedirect("/user/dashboard");
//                        }
//
//                    })
//                    .failureUrl("/user/login?error=true")
//                    .permitAll()
//            )
//
////            .logout(logout -> logout
////                    .logoutUrl("/logout")
////                    .logoutSuccessUrl("/")
////                    .invalidateHttpSession(true)
////                    .deleteCookies("JSESSIONID")
////                );
//            .logout(logout -> logout
//                    .logoutUrl("/logout")
//                    .logoutSuccessHandler((request, response, authentication) -> {
//
//                        if (authentication != null &&
//                            authentication.getAuthorities().stream()
//                            .anyMatch(a -> a.getAuthority().getAuthority().equals("ROLE_ADMIN"))) {
//
//                            response.sendRedirect("/admin/login?logout");
//
//                        } else {
//
//                            response.sendRedirect("/user/login?logout");
//                        }
//
//                    })
//                    .invalidateHttpSession(true)
//                    .deleteCookies("JSESSIONID")
//                    .permitAll()
//            )
//            // ✅ IMPORTANT PART (Single Session)
//            .sessionManagement(session -> session
//                    .maximumSessions(1)
//                    .maxSessionsPreventsLogin(false)
//            );
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
////    @Bean
////    public UserDetailsService userDetailsService(UserRepository userRepository) {
////        return username -> {
////            User user = userRepository.findByEmail(username);
////
////            if (user == null) {
////                throw new UsernameNotFoundException("User not found");
////            }
////
////            else if (!user.isActive()) {
////                throw new UsernameNotFoundException("Account not activated by admin");
////            }
////
////            return new org.springframework.security.core.userdetails.User(
////                user.getEmail(),
////                user.getPassword(),
////                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
////            );
////        };
////    }
//    
//    @Bean
//    public UserDetailsService userDetailsService(UserRepository userRepository,
//                                                 AdminRepository adminRepository) {
//
//        return username -> {
//
//            // First check Admin
//            Admin admin = adminRepository.findByEmail(username);
//            if (admin != null) {
//                return new org.springframework.security.core.userdetails.User(
//                        admin.getEmail(),
//                        admin.getPassword(),
//                        Collections.singletonList(
//                                new SimpleGrantedAuthority("ROLE_ADMIN")
//                        )
//                );
//            }
//
//            // Then check User
//            User user = userRepository.findByEmail(username);
//            if (user != null && user.isActive()) {
//                return new org.springframework.security.core.userdetails.User(
//                        user.getEmail(),
//                        user.getPassword(),
//                        Collections.singletonList(
//                                new SimpleGrantedAuthority("ROLE_USER")
//                        )
//                );
//            }
//
//            throw new UsernameNotFoundException("User not found");
//        };
//    }
//
//
//}

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

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
        .csrf(csrf -> csrf.disable())

        .authorizeHttpRequests(auth -> auth

            .requestMatchers("/", "/images/**", "/css/**", "/js/**").permitAll()

            .requestMatchers("/user/login","/user/register",
                    "/user/forgot-password","/user/reset-password").permitAll()

            .requestMatchers("/admin/login",
                    "/admin/forgot-password","/admin/reset-password").permitAll()

            .requestMatchers("/admin/**").hasRole("ADMIN")
            .requestMatchers("/user/**").hasRole("USER")
            .requestMatchers("/admin/logout").hasRole("ADMIN")
            .requestMatchers("/user/logout").hasRole("USER")

            .anyRequest().authenticated()
        )

        .formLogin(form -> form
                .loginPage("/user/login")
                .loginProcessingUrl("/login")

                .successHandler((request,response,authentication)->{

                    boolean isAdmin = authentication.getAuthorities()
                            .stream()
                            .anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"));

                    if(isAdmin){
                        response.sendRedirect("/admin/dashboard");
                    }else{
                        response.sendRedirect("/user/dashboard");
                    }

                })

                .failureHandler((request,response,exception)->{

                    String referer = request.getHeader("Referer");

                    if(referer != null && referer.contains("/admin/login")){
                        response.sendRedirect("/admin/login?error");
                    }else{
                        response.sendRedirect("/user/login?error");
                    }

                })

                .permitAll()
        )

//        .logout(logout -> logout
//                .logoutUrl("/logout")
//                .logoutSuccessHandler((request, response, authentication) -> {
//
//                    if (authentication != null &&
//                        authentication.getAuthorities().stream()
//                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
//
//                        response.sendRedirect("/admin/login?logout");
//
//                    } else {
//
//                        response.sendRedirect("/user/login?logout");
//                    }
//
//                })
//                .invalidateHttpSession(true)
//                .clearAuthentication(true)
//                .deleteCookies("JSESSIONID")
//                .permitAll()
//        ) 	
        .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessHandler((request, response, authentication) -> {

                    String type = request.getParameter("logoutType");

                    if ("admin".equals(type)) {
                        response.sendRedirect("/admin/login?logout");
                    } else {
                        response.sendRedirect("/user/login?logout");
                    }

                })
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
        )
        .exceptionHandling(ex -> ex

        	    .authenticationEntryPoint((request, response, authException) -> {

        	        String uri = request.getRequestURI();

        	        if (uri.startsWith("/admin")) {
        	            response.sendRedirect("/admin/login");
        	        } else if (uri.startsWith("/user")) {
        	            response.sendRedirect("/user/login");
        	        } else {
        	            response.sendRedirect("/user/login");
        	        }

        	    })

        	    .accessDeniedHandler((request, response, accessDeniedException) -> {

        	        String uri = request.getRequestURI();

        	        if (uri.startsWith("/admin")) {
        	            response.sendRedirect("/admin/login");
        	        } else if (uri.startsWith("/user")) {
        	            response.sendRedirect("/user/login");
        	        } else {
        	            response.sendRedirect("/user/login");
        	        }

        	    })
        	)
        .sessionManagement(session -> session
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)

                .expiredSessionStrategy(event -> {

                    if(event.getRequest().isUserInRole("ADMIN")){
                        event.getResponse().sendRedirect("/admin/login?sessionExpired");
                    }else{
                        event.getResponse().sendRedirect("/user/login?sessionExpired");
                    }

                })
        );

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public UserDetailsService userDetailsService(
            UserRepository userRepository,
            AdminRepository adminRepository){

        return username -> {

            Admin admin = adminRepository.findByEmail(username);

            if(admin != null){
                return new org.springframework.security.core.userdetails.User(
                        admin.getEmail(),
                        admin.getPassword(),
                        Collections.singletonList(
                                new SimpleGrantedAuthority("ROLE_ADMIN"))
                );
            }

            User user = userRepository.findByEmail(username);

            if(user != null && user.isActive()){
                return new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        Collections.singletonList(
                                new SimpleGrantedAuthority("ROLE_USER"))
                );
            }

            throw new UsernameNotFoundException("User not found");
        };
    }
}
