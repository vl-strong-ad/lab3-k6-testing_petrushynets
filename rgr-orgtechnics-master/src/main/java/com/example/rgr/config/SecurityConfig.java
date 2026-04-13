package com.example.rgr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Base64;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Дозволяє використовувати @PreAuthorize в контролерах
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll() // Доступ до картинок/стилів
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Тільки адмін може заходити на /admin
                        .anyRequest().authenticated() // Всі інші сторінки вимагають входу
                )
                .formLogin((form) -> form
                        .loginPage("/login") // Вказуємо нашу сторінку входу
                        .defaultSuccessUrl("/shops", true) // Куди перенаправляти після успішного входу
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    // Вимога на "відмінно": Кодування паролів у Base64
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                // Кодуємо пароль у Base64
                return Base64.getEncoder().encodeToString(rawPassword.toString().getBytes());
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                // Перевіряємо, чи співпадає введений пароль з тим, що в базі
                String encodedRaw = encode(rawPassword);
                return encodedRaw.equals(encodedPassword);
            }
        };
    }
}