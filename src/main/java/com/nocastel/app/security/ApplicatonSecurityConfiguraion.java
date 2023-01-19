package com.nocastel.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ApplicatonSecurityConfiguraion {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicatonSecurityConfiguraion(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/index.html", "/css/*", "/js/*").permitAll()
                        .requestMatchers("/api/**").hasRole(ApplicationUserRole.STUDENT.name())/*role based auth */
                        .anyRequest().authenticated())
                .httpBasic();
        return http.build();
    }

    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails userJaneDoe = User.builder()
                .username("janedoe")
                .password(passwordEncoder.encode("password"))
                .roles(ApplicationUserRole.STUDENT.name())
                .build();
        UserDetails kevin = User.builder()
                .username("kevin")
                .password(passwordEncoder.encode("admin123"))
                .roles(ApplicationUserRole.ADMIN.name())
                .build();
        return new InMemoryUserDetailsManager(userJaneDoe, kevin);
    }
}
