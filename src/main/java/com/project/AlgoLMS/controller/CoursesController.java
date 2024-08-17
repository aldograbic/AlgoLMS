package com.project.AlgoLMS.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/courses")
public class CoursesController {
    
    @GetMapping
    public String getCoursesPage() {
        return "courses";
    }

    @GetMapping("/add")
    public String getAddCoursePage() {
        return "add-course";
    }
}