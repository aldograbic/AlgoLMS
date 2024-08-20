package com.project.AlgoLMS.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.AlgoLMS.model.course.Course;
import com.project.AlgoLMS.model.enrollment.Enrollment;
import com.project.AlgoLMS.model.user.User;
import com.project.AlgoLMS.model.userProfile.UserProfile;
import com.project.AlgoLMS.repository.course.CourseRepository;
import com.project.AlgoLMS.repository.enrollment.EnrollmentRepository;
import com.project.AlgoLMS.repository.user.UserRepository;
import com.project.AlgoLMS.service.FileUploadService;

@Controller
@RequestMapping("/courses")
public class CoursesController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private FileUploadService fileUploadService;
    
    @GetMapping
    public String getCoursesPage(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByEmail(username);
    
        List<Course> courses = courseRepository.getCourses();
    
        for (Course course : courses) {
            boolean isEnrolled = enrollmentRepository.existsByUserIdAndCourseId(user.getUserId(), course.getCourseId());
            course.setEnrolled(isEnrolled);
        }
    
        model.addAttribute("courses", courses);
        return "courses/courses";
    }
    

    @GetMapping("/add")
    public String getAddCoursePage() {
        return "courses/add-course";
    }

    @PostMapping("/add")
public String addCourse(@RequestParam("title") String title,
                        @RequestParam("description") String description,
                        @RequestParam(value = "coverPhoto", required = false) MultipartFile coverPhoto,
                        @RequestParam(value = "accessType", defaultValue = "public") String accessType,
                        @RequestParam(value = "accessCode", required = false) String accessCode,
                        @RequestParam("instructorId") Long instructorId,
                        RedirectAttributes redirectAttributes) {
    try {
        // Handle cover photo upload
        String coverPhotoUrl = null;
        if (coverPhoto != null && !coverPhoto.isEmpty()) {
            coverPhotoUrl = fileUploadService.uploadFile(coverPhoto);
        }

        // Process access type and code
        if ("on".equals(accessType)) {
            accessCode = null;
            accessType = "private";
        } else {
            accessType = "public";

            if (accessCode == null || accessCode.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Za privatne tečajeve potreban je pristupni kod.");
                return "redirect:/courses/add";
            }
        }

        Course course = new Course();
        course.setTitle(title);
        course.setDescription(description);
        course.setCoverPhoto(coverPhotoUrl);
        course.setAccessType(accessType);
        course.setAccessCode(accessCode);
        course.setInstructorId(instructorId);

        courseRepository.save(course);
        redirectAttributes.addFlashAttribute("message", "Tečaj uspješno dodan!");

    } catch (IOException e) {
        redirectAttributes.addFlashAttribute("error", "Došlo je do pogreške pri uploadu fotografije.");
        return "redirect:/courses/add";
    }

    return "redirect:/courses";
}


    @PostMapping("/enroll")
    public String enrollInCourse(@RequestParam("courseId") Long courseId, RedirectAttributes redirectAttributes) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByEmail(username);

        Course course = courseRepository.findById(courseId);

        if (course == null) {
            redirectAttributes.addFlashAttribute("error", "Tečaj nije pronađen.");
            return "redirect:/courses";
        }

        if (enrollmentRepository.existsByUserIdAndCourseId(user.getUserId(), courseId)) {
            redirectAttributes.addFlashAttribute("info", "Već ste upisani na ovaj tečaj.");
            return "redirect:/courses";
        }

        enrollmentRepository.save(new Enrollment(user.getUserId(), courseId));
        redirectAttributes.addFlashAttribute("success", "Uspješno ste upisani na tečaj!");

        return "redirect:/courses/" + courseId;
    }

    @PostMapping("/enrollWithAccessCode")
    public String enrollWithAccessCodeInCourse(@RequestParam("courseId") Long courseId, @RequestParam("accessCode") String accessCode, RedirectAttributes redirectAttributes) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByEmail(username);

        Course course = courseRepository.findById(courseId);

        if (course == null) {
            redirectAttributes.addFlashAttribute("error", "Tečaj nije pronađen.");
            return "redirect:/courses";
        }

        if (enrollmentRepository.existsByUserIdAndCourseId(user.getUserId(), courseId)) {
            redirectAttributes.addFlashAttribute("info", "Već ste upisani na ovaj tečaj.");
            return "redirect:/courses";
        }

        if (!accessCode.equals(course.getAccessCode())) {
            redirectAttributes.addFlashAttribute("error", "Pristupni kod je pogrešan.");
            return "redirect:/courses";
        }

        enrollmentRepository.save(new Enrollment(user.getUserId(), courseId));
        redirectAttributes.addFlashAttribute("success", "Uspješno ste upisani na tečaj!");

        return "redirect:/courses/" + courseId;
    }

    @GetMapping("/{courseId}")
    public String getCoursePage(@PathVariable("courseId") Long courseId, Model model, RedirectAttributes redirectAttributes) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByEmail(username);

        if (!enrollmentRepository.existsByUserIdAndCourseId(user.getUserId(), courseId)) {
            redirectAttributes.addFlashAttribute("error", "Niste upisani u ovaj tečaj.");
            return "redirect:/courses";
        }

        Course course = courseRepository.findById(courseId);
        model.addAttribute("course", course);

        UserProfile instructorProfile = userRepository.getUserProfileByUserId(course.getInstructor().getUserId());
        model.addAttribute("instructorProfile", instructorProfile);

        return "courses/course";
    }

    @PostMapping("/{courseId}/changeAccessCode")
    public String changeAccessCode(@PathVariable("courseId") Long courseId, @RequestParam("accessCode") String accessCode, RedirectAttributes redirectAttributes) {
        courseRepository.changeAccessCodeByCourseId(accessCode, courseId);
        redirectAttributes.addFlashAttribute("success", "Pristupni kod uspješno promijenjen!");

        return "redirect:/courses/" + courseId;
    }

    @GetMapping("/my")
    public String getMyCoursesPage(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByEmail(username);

        List<Course> courses = courseRepository.findCoursesByUserId(user.getUserId());
        for (Course course : courses) {
            boolean isEnrolled = enrollmentRepository.existsByUserIdAndCourseId(user.getUserId(), course.getCourseId());
            course.setEnrolled(isEnrolled);
        }
        model.addAttribute("courses", courses);

        return "courses/my-courses";
    }
}