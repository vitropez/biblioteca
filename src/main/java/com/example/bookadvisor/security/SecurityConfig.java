package com.example.bookadvisor.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

        @Bean
        public AuthenticationManager authenticationManager(
                        AuthenticationConfiguration authenticationConfiguration)
                        throws Exception {
                return authenticationConfiguration.getAuthenticationManager();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

                http.headers(headersConfigurer -> headersConfigurer
                                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

                http.authorizeHttpRequests(auth -> auth
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                .requestMatchers("/signin", "/signup", "/public/**", "/libros", "/generos",
                                                "/perform_login", "/files/**")
                                .permitAll()
                                .requestMatchers("/valoraciones/**","/libros/**").hasAnyRole("USER", "MANAGER", "ADMIN")
                                .requestMatchers( "/generos/**").hasAnyRole("MANAGER", "ADMIN")
                                .requestMatchers("/**").hasRole("ADMIN"))
                                .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer
                                                .loginPage("/signin")
                                                .loginProcessingUrl("/perform_login")
                                                .failureUrl("/signin?error")
                                                .defaultSuccessUrl("/public", true).permitAll())
                                .logout(logout -> logout
                                                .logoutSuccessUrl("/signin?logout").permitAll())
                                .csrf(csrf -> csrf.disable())
                                .httpBasic(Customizer.withDefaults());

                http.exceptionHandling(exceptions -> exceptions.accessDeniedPage("/public/accessError"));

                return http.build();
        }

}
