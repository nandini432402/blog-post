package com.example.blogNest.domain.blog;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**", "/public/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(org.springframework.security.config.Customizer.withDefaults())
            .httpBasic(org.springframework.security.config.Customizer.withDefaults());
        return http.build();
    }
}