package com.project.AlgoLMS.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/assigments")
public class AssigmentsController {
    
    @GetMapping("/grades")
    public String getAssigmentGradesPage() {
        return "assigment-grades";
    }

    // @GetMapping("/my")
    // public String getMyAssigmentsPage() {
    //     return "assigments";
    // }

    @GetMapping("/add")
    public String getAddAssigmentPage() {
        return "add-assigment";
    }
}