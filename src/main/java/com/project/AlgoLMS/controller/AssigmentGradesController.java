package com.project.AlgoLMS.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AssigmentGradesController {
    
    @GetMapping("/assigment-grades")
    public String getAssigmentGrades() {
        return "assigment-grades";
    }
}