package com.project.AlgoLMS.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.AlgoLMS.service.UserService;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam("email") String email, Model model) {
        if (userService.registerUser(email)) {
            model.addAttribute("message", "Registracija uspješna! Provjerite svoju e-poštu za potvrdu.");
        } else {
            model.addAttribute("message", "Račun s ovom e-mail adresom je već registriran.");
        }
        return "login";
    }
}

