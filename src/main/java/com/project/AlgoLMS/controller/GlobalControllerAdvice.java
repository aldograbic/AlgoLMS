package com.project.AlgoLMS.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.project.AlgoLMS.model.user.User;
import com.project.AlgoLMS.model.userProfile.UserProfile;
import com.project.AlgoLMS.repository.user.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final UserRepository userRepository;

    public GlobalControllerAdvice(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @ModelAttribute
    public void addGlobalAttributes(Model model, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            
            User user = userRepository.findByEmail(username);

            if (user != null) {

                UserProfile userProfile = userRepository.getUserProfileByUserId(user.getUserId());
                model.addAttribute("userProfile", userProfile);
                model.addAttribute("user", user);
            }
        }

        String currentUri = request.getRequestURI();
        model.addAttribute("currentUri", currentUri);
    }
}