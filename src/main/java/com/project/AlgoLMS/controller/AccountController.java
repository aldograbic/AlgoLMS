package com.project.AlgoLMS.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.AlgoLMS.model.user.User;
import com.project.AlgoLMS.model.userProfile.UserProfile;
import com.project.AlgoLMS.repository.user.UserRepository;
import com.project.AlgoLMS.service.FileUploadService;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
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

    @PostMapping("/update")
    public String updateAccount(@RequestParam("fullName") String fullName,
                                @RequestParam(value = "password", required = false) String password,
                                @RequestParam(value = "newPassword", required = false) String newPassword,
                                @RequestParam("gender") String gender,
                                @RequestParam(value = "phone", required = false) String phone,
                                @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture,
                                @RequestParam(value = "bio", required = false) String bio,
                                RedirectAttributes redirectAttributes) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User authUser = userRepository.findByEmail(username);

        if (newPassword != null && !newPassword.isEmpty()) {
            if (password == null || !passwordEncoder.matches(password, authUser.getPassword())) {
                redirectAttributes.addFlashAttribute("error", "Pogrešna trenutna lozinka. Pokušajte opet!");
                return "redirect:/account";
            }

            authUser.setPassword(passwordEncoder.encode(newPassword));
        }

        String profilePictureUrl = null;
        if (profilePicture != null && !profilePicture.isEmpty()) {
            try {
                profilePictureUrl = fileUploadService.uploadFile(profilePicture);
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("error", "Došlo je do pogreške prilikom prijenosa fotografije.");
                return "redirect:/account";
            }
        }

        authUser.setFullName(fullName);
        authUser.setGender(gender);
        authUser.setPhone(phone);

        UserProfile userProfile = userRepository.getUserProfileByUserId(authUser.getUserId());
        if (userProfile == null) {
            userProfile = new UserProfile();
            userProfile.setUser(authUser);
        }

        userProfile.setProfilePicture(profilePictureUrl != null ? profilePictureUrl : userProfile.getProfilePicture());
        userProfile.setBio(bio);

        try {
            userRepository.update(authUser);
            userRepository.updateUserProfileDetails(userProfile);
            redirectAttributes.addFlashAttribute("success", "Račun uspješno ažuriran");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Došlo je do pogreške prilikom ažuriranja računa.");
            return "redirect:/account";
        }

        return "redirect:/account";
    }

    @GetMapping("/settings")
    public String getAccountSettingsPage() {
        return "settings";
    }
}