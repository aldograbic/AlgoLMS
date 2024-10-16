package com.project.AlgoLMS.config;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import com.project.AlgoLMS.model.user.User;
import com.project.AlgoLMS.repository.user.UserRepository;


@Service
public class CustomUserDetailsService extends SavedRequestAwareAuthenticationSuccessHandler implements UserDetailsService {
    
    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found.");
        }

        if (!user.isEmailVerified()) {
            throw new EmailNotVerifiedException("Email not verified.");
        }

        return new org.springframework.security.core.userdetails.User(
                email,
                user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(user.getRole()))
        );
    }
}