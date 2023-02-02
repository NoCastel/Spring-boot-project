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
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                /*
                 * //these lines are needed for the csrf protection, for now csrf is disabled.
                 * CookieCsrfTokenRepository tokenRepository =
                 * CookieCsrfTokenRepository.withHttpOnlyFalse();
                 * XorCsrfTokenRequestAttributeHandler delegate = new
                 * XorCsrfTokenRequestAttributeHandler();
                 * delegate.setCsrfRequestAttributeName("_csrf");
                 * CsrfTokenRequestHandler requestHandler = delegate::handle;
                 */
                http
                                .csrf((csrf) -> csrf.disable()
                                // Enable later
                                // .csrfTokenRepository(tokenRepository)
                                // .csrfTokenRequestHandler(requestHandler)
                                )
                                .authorizeHttpRequests(authorize -> authorize
                                                .requestMatchers("/index.html", "/css/*", "/js/*", "/login").permitAll()
                                                .requestMatchers("/api/**")
                                                .hasRole(ApplicationUserRole.STUDENT.name())
                                                .anyRequest().authenticated()
                                                ) 
                                .formLogin().loginPage("/login")
                                .defaultSuccessUrl("/courses", true)
                                ;
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
