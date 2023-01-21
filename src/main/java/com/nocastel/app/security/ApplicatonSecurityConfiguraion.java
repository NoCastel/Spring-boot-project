package com.nocastel.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static com.nocastel.app.security.ApplicationUserPermission.*;
import static com.nocastel.app.security.ApplicationUserRole.*;

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
                                .csrf().disable()
                                .authorizeHttpRequests(authorize -> authorize
                                                .requestMatchers("/index.html", "/css/*", "/js/*").permitAll()
                                                .requestMatchers("/api/**")
                                                .hasRole(ApplicationUserRole.STUDENT.name())/* role based auth */
                                                /*authorities*/
                                                .requestMatchers(HttpMethod.DELETE, "/managment/api/**")
                                                .hasAuthority(COURSE_WRITE.name())
                                                .requestMatchers(HttpMethod.POST, "/managment/api/**")
                                                .hasAuthority(COURSE_WRITE.name())
                                                .requestMatchers(HttpMethod.PUT, "/managment/api/**")
                                                .hasAuthority(COURSE_WRITE.name())
                                                .requestMatchers(HttpMethod.GET, "/managment/api/**")
                                                // .hasAnyRole(ADMIN.name(), TRAINEE.name())
                                                .anyRequest().authenticated())
                                .httpBasic();
                return http.build();
        }

        @Bean
        protected UserDetailsService userDetailsService() {
                UserDetails userJaneDoe = User.builder()
                                .username("janedoe")
                                .password(passwordEncoder.encode("student123"))
                                .roles(ApplicationUserRole.STUDENT.name())
                                .build();
                UserDetails kevin = User.builder()
                                .username("kevin")
                                .password(passwordEncoder.encode("admin123"))
                                .roles(ApplicationUserRole.ADMIN.name())
                                .build();
                UserDetails angela = User.builder()
                                .username("angela")
                                .password(passwordEncoder.encode("trainee123"))
                                .roles(ApplicationUserRole.TRAINEE.name())
                                .build();
                return new InMemoryUserDetailsManager(userJaneDoe, kevin, angela);
        }
}
