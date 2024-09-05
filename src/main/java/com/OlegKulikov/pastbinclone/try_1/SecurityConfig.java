package com.OlegKulikov.pastbinclone.try_1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/", "/texts/**", "/login", "/register").permitAll()  // Разрешить доступ к определенным страницам
                                .anyRequest().authenticated()  // Все остальные запросы требуют аутентификации
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login")  // Указание страницы логина
                                .defaultSuccessUrl("/user_page", true)  // Переход после успешного логина
                                .permitAll()
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/logout") // URL для выхода
                                .logoutSuccessUrl("/login?logout") // URL после успешного выхода
                                .permitAll() // Разрешение всем доступ к выходу
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
