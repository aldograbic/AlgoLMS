package com.project.AlgoLMS.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    private DatabaseLoginFailureHandler databaseLoginFailureHandler;
     
    @Autowired
    private DatabaseLoginSuccessHandler databaseLoginSuccessHandler;
    
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
	private PasswordEncoder passwordEncoder;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf((csrf -> csrf
                .disable()))
            .authorizeHttpRequests((auth) -> auth
                .requestMatchers("/css/**", "/img/**", "/js/**").permitAll()
                .requestMatchers("/", "/login", "/register", "/confirm", "/setup", "/privacy-policy", "/terms-and-conditions").permitAll()
                // .requestMatchers("/user-dashboard").hasRole("USER")
                // .requestMatchers("/admin-dashboard").hasRole("ADMIN")
                .requestMatchers("/courses/add", "/courses/{courseId}/changeAccessCode", "/courses/{courseId}/addResources", "/assigments/add").hasAnyRole("ADMIN", "INSTRUCTOR")
                .anyRequest().authenticated()
            )
            .formLogin((formLogin) -> formLogin
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .failureHandler(databaseLoginFailureHandler)
                .successHandler(databaseLoginSuccessHandler)
                .permitAll()
            )
            .logout((logout) -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/?successLogout")
                .invalidateHttpSession(true)
                .permitAll())
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}