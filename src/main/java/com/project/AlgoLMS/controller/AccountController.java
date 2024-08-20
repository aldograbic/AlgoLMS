package com.project.AlgoLMS.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    // @PostMapping("/update")
    // public String updateAccount(@RequestParam("fullName") String fullName,
    //                             @RequestParam("password") String password,
    //                             @RequestParam("newPassword") String newPassword,
    //                             @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture,
    //                             @RequestParam(value = "phone", required = false) String phone,
    //                             @RequestParam("gender") String gender,
    //                             @RequestParam(value = "bio", required = false) String bio,
    //                             RedirectAttributes redirectAttributes) {

    //     if (password.equals()) {
    //         User user = new User();
    //         user.setFullName(fullName);
    //         user.

    //         try {
    //             userRepository.update(user);
    //             userRepository.updateUserProfileDetails(userProfile);
    //             redirectAttributes.addFlashAttribute("success", "Račun uspješno ažuriran!");
    
    //         } catch (Exception e) {
    //             redirectAttributes.addFlashAttribute("error", "Došlo je do pogreške pri ažuriranju računa.");
    //             return "redirect:/account";
    //         }

    //     } else {
    //         redirectAttributes.addFlashAttribute("error", "Pogrešna lozinka. Pokušajte ponovno!");
    //         return "redirect:/account";
    //     }

    //     return "redirect:/account";
    // }
}