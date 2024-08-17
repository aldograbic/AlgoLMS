package com.project.AlgoLMS.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.AlgoLMS.model.user.User;
import com.project.AlgoLMS.model.userProfile.UserProfile;
import com.project.AlgoLMS.repository.user.UserRepository;
import com.project.AlgoLMS.service.EmailService;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/setup")
    public String showSetupPage(@RequestParam("token") String token, Model model) {
        User user = userRepository.findByConfirmationToken(token);
        if (user != null && user.isEmailVerified()) {
            model.addAttribute("token", token);
            return "setup";
        } else {
            model.addAttribute("message", "Nevažeći token, molimo kontaktirajte podršku.");
            return "login";
        }
    }

    @PostMapping("/setup")
    public String setupUser(@RequestParam("token") String token,
                            @RequestParam("fullName") String fullName,
                            @RequestParam("password") String password,
                            @RequestParam(value = "bio", required = false) String bio,
                            @RequestParam(value = "profilePicture", required = false) String profilePicture,
                            Model model) {
        User user = userRepository.findByConfirmationToken(token);
        if (user != null && user.isEmailVerified()) {

            String encyptedPassword = passwordEncoder.encode(password);
            user.setPassword(encyptedPassword);
            user.setFullName(fullName);
            user.setConfirmationToken(null);
            userRepository.saveFull(user);

            UserProfile userProfile = new UserProfile();
            userProfile.setUserId(user.getUserId());
            userProfile.setBio(bio);
            userProfile.setProfilePicture(profilePicture);
            userRepository.saveUserProfileDetails(userProfile);

            model.addAttribute("successMessage", "Registracija završena! Sada se možete prijaviti.");
            return "login";
        } else {
            model.addAttribute("message", "Nevažeći token, molimo kontaktirajte podršku.");
            return "login";
        }
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam("email") String email, Model model) {
        
        if (userRepository.existsByEmail(email)) {
            model.addAttribute("successMessage", "Račun s ovom e-mail adresom je već registriran.");
            return "login";

        } else {
            String confirmationToken = UUID.randomUUID().toString();

            User user = new User();
            user.setEmail(email);
            user.setConfirmationToken(confirmationToken);
            userRepository.save(user);

            String confirmationLink = "http://localhost:8080/confirm?token=" + confirmationToken;
            emailService.sendConfirmationEmail(email, confirmationLink);
            model.addAttribute("message", "Registracija uspješna! Provjerite svoju e-poštu za potvrdu.");
        }
        return "login";
    }

    @GetMapping("/confirm")
    public String confirmUser(@RequestParam("token") String token, Model model) {
        User user = userRepository.findByConfirmationToken(token);

        if (user != null) {
            user.setEmailVerified(true);
            userRepository.update(user);
            model.addAttribute("message", "Vaš račun je uspješno potvrđen!");
            return "setup";
        } else {
            model.addAttribute("message", "Nevažeći link za potvrdu, molimo kontaktirajte podršku.");
            return "login";
        }
    }
}