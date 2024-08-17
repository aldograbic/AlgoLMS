package com.project.AlgoLMS.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.AlgoLMS.model.course.Course;
import com.project.AlgoLMS.repository.course.CourseRepository;

@Controller
@RequestMapping("/courses")
public class CoursesController {

    @Autowired
    private CourseRepository courseRepository;
    
    @GetMapping
    public String getCoursesPage(Model model) {

        List<Course> courses = courseRepository.getCourses();
        model.addAttribute("courses", courses);

        return "courses";
    }

    @GetMapping("/add")
    public String getAddCoursePage() {
        return "add-course";
    }

    @PostMapping("/add")
    public String addCourse(@ModelAttribute Course course, RedirectAttributes redirectAttributes) {
    
        if ("on".equals(course.getAccessType())) { // Assuming the checkbox sends "on" when checked
            course.setAccessCode(null);
            course.setAccessType("public");
        } else {
            course.setAccessType("private");

            if (course.getAccessCode() == null || course.getAccessCode().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Za privatne tečajeve potreban je pristupni kod.");
                return "redirect:/courses/add";
            }
        }
    
        courseRepository.save(course);
        redirectAttributes.addFlashAttribute("message", "Tečaj uspješno dodan!");
    
        return "redirect:/courses";
    }
}