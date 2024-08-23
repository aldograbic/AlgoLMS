package com.project.AlgoLMS.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {
    
    @GetMapping("/login")
    public String getLoginPage(RedirectAttributes redirectAttributes) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            redirectAttributes.addFlashAttribute("info", "Već ste prijavljeni u platformu!");
            return "redirect:/dashboard";
        }

        return "login";
    }
}