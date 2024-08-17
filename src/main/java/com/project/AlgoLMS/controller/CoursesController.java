package com.project.AlgoLMS.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CoursesController {
    
    @GetMapping("/courses")
    public String getCoursesPage() {
        return "courses";
    }
}