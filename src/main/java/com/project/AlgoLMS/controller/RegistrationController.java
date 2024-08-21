package com.project.AlgoLMS.controller;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.AlgoLMS.model.user.User;
import com.project.AlgoLMS.model.userProfile.UserProfile;
import com.project.AlgoLMS.repository.user.UserRepository;
import com.project.AlgoLMS.service.EmailService;
import com.project.AlgoLMS.service.FileUploadService;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private FileUploadService fileUploadService;

    @GetMapping("/setup")
    public String showSetupPage(@RequestParam("token") String token, Model model) {
        User user = userRepository.findByConfirmationToken(token);
        if (user != null && user.isEmailVerified()) {
            model.addAttribute("token", token);
            return "setup";
        } else {
            model.addAttribute("error", "Nevažeći token, molimo kontaktirajte podršku.");
            return "login";
        }
    }

    @PostMapping("/setup")
    public String setupUser(@RequestParam("token") String token,
                            @RequestParam("fullName") String fullName,
                            @RequestParam("password") String password,
                            @RequestParam("gender") String gender,
                            @RequestParam(value = "phone", required = false) String phone,
                            @RequestParam(value = "bio", required = false) String bio,
                            @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture,
                            RedirectAttributes redirectAttributes,
                            Model model) {

        User user = userRepository.findByConfirmationToken(token);
        if (user != null && user.isEmailVerified()) {

            String encyptedPassword = passwordEncoder.encode(password);
            user.setPassword(encyptedPassword);
            user.setFullName(fullName);
            user.setConfirmationToken(null);
            userRepository.saveFull(user);

            String profilePictureUrl = null;
            if (profilePicture != null && !profilePicture.isEmpty()) {
                try {
                    profilePictureUrl = fileUploadService.uploadFile(profilePicture);
                } catch (IOException e) {
                    redirectAttributes.addFlashAttribute("error", "Došlo je do pogreške prilikom prijenosa fotografije.");
                    return "redirect:/account";
                }
            }

            UserProfile userProfile = new UserProfile();
            userProfile.setUserId(user.getUserId());
            userProfile.setBio(bio);
            userProfile.setProfilePicture(profilePictureUrl);
            userRepository.saveUserProfileDetails(userProfile);

            model.addAttribute("success", "Registracija završena! Sada se možete prijaviti.");
            return "login";
        } else {
            model.addAttribute("error", "Nevažeći token, molimo kontaktirajte podršku.");
            return "login";
        }
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam("email") String email, Model model) {
        
        if (userRepository.existsByEmail(email)) {
            model.addAttribute("info", "Račun s ovom e-mail adresom je već registriran.");
            return "login";

        } else {
            String confirmationToken = UUID.randomUUID().toString();

            User user = new User();
            user.setEmail(email);
            user.setConfirmationToken(confirmationToken);
            userRepository.save(user);

            String confirmationLink = "http://localhost:8080/confirm?token=" + confirmationToken;
            emailService.sendConfirmationEmail(email, confirmationLink);
            model.addAttribute("success", "Registracija uspješna! Provjerite svoju e-poštu za potvrdu.");
        }
        return "login";
    }

    @GetMapping("/confirm")
    public String confirmUser(@RequestParam("token") String token, Model model) {
        User user = userRepository.findByConfirmationToken(token);

        if (user != null) {
            user.setEmailVerified(true);
            userRepository.updateVerification(user);
            model.addAttribute("success", "Vaš račun je uspješno potvrđen!");
            return "setup";
        } else {
            model.addAttribute("error", "Nevažeći link za potvrdu, molimo kontaktirajte podršku.");
            return "login";
        }
    }
}