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
import org.springframework.security.web.DefaultSecurityFilterChain;
// import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
// import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;

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
        DefaultSecurityFilterChain springSecurity(HttpSecurity http) throws Exception {
                CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
                // set the name of the attribute the CsrfToken will be populated on
                requestHandler.setCsrfRequestAttributeName("_csrf");
                http
                                // ...
                                .csrf((csrf) -> csrf
                                                .csrfTokenRequestHandler(requestHandler));
                return http.build();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                CookieCsrfTokenRepository tokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
                XorCsrfTokenRequestAttributeHandler delegate = new XorCsrfTokenRequestAttributeHandler();
                // set the name of the attribute the CsrfToken will be populated on
                delegate.setCsrfRequestAttributeName("_csrf");
                // Use only the handle() method of XorCsrfTokenRequestAttributeHandler and the
                // default implementation of resolveCsrfTokenValue() from
                // CsrfTokenRequestHandler
                CsrfTokenRequestHandler requestHandler = delegate::handle;

                http

                                // .csrf(csrf -> csrf.disable() )
                                .csrf((csrf) -> csrf
                                                .csrfTokenRepository(tokenRepository)
                                                .csrfTokenRequestHandler(requestHandler))
                                .authorizeHttpRequests(authorize -> authorize
                                                .requestMatchers("/index.html", "/css/*", "/js/*").permitAll()
                                                .requestMatchers("/api/**")
                                                .hasRole(ApplicationUserRole.STUDENT.name())
                                                .anyRequest().authenticated())
                                // .requestMatchers(HttpMethod.DELETE,
                                // "/managment/api/**").hasAuthority(COURSE_WRITE.getPermission())
                                .httpBasic();
                return http.build();
        }

        @Bean
        protected UserDetailsService userDetailsService() {
                UserDetails userJaneDoe = User.builder()
                                .username("janedoe")
                                .password(passwordEncoder.encode("student123"))
                                // .roles(ApplicationUserRole.STUDENT.name())
                                .authorities(STUDENT.grantedAuthorities())
                                .build();
                UserDetails kevin = User.builder()
                                .username("kevin")
                                .password(passwordEncoder.encode("admin123"))
                                // .roles(ApplicationUserRole.ADMIN.name())
                                .authorities(ADMIN.grantedAuthorities())
                                .build();
                UserDetails angela = User.builder()
                                .username("angela")
                                .password(passwordEncoder.encode("trainee123"))
                                // .roles(ApplicationUserRole.TRAINEE.name())
                                .authorities(TRAINEE.grantedAuthorities())
                                .build();
                return new InMemoryUserDetailsManager(userJaneDoe, kevin, angela);
        }
}
