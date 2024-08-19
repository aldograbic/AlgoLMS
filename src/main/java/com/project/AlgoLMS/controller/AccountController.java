package com.project.AlgoLMS.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.AlgoLMS.model.user.User;
import com.project.AlgoLMS.model.userProfile.UserProfile;
import com.project.AlgoLMS.repository.user.UserRepository;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private UserRepository userRepository;
    
    @GetMapping
    public String getAccountPage(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByEmail(username);
        model.addAttribute("user", user);

        UserProfile userProfile = userRepository.getUserProfileByUserId(user.getUserId());
        model.addAttribute("userProfile", userProfile);
        
        return "account";
    }
}