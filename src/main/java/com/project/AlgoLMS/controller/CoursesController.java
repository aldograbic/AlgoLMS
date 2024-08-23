package com.project.AlgoLMS.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.project.AlgoLMS.config.TimeAgoFormatter;
import com.project.AlgoLMS.model.course.Course;
import com.project.AlgoLMS.model.courseResource.CourseResource;
import com.project.AlgoLMS.model.enrollment.Enrollment;
import com.project.AlgoLMS.model.forum.Forum;
import com.project.AlgoLMS.model.forum.ForumPost;
import com.project.AlgoLMS.model.forum.ForumPostReply;
import com.project.AlgoLMS.model.user.User;
import com.project.AlgoLMS.model.userProfile.UserProfile;
import com.project.AlgoLMS.repository.course.CourseRepository;
import com.project.AlgoLMS.repository.enrollment.EnrollmentRepository;
import com.project.AlgoLMS.repository.forum.ForumRepository;
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

    @Autowired
    private ForumRepository forumRepository;
    
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
                            @RequestParam(value = "accessType") String accessType,
                            @RequestParam(value = "accessCode", required = false) String accessCode,
                            @RequestParam("instructorId") Long instructorId,
                            RedirectAttributes redirectAttributes) {
        try {
            String coverPhotoUrl = null;
            if (coverPhoto != null && !coverPhoto.isEmpty()) {
                coverPhotoUrl = fileUploadService.uploadFile(coverPhoto);
            }

            if ("private".equals(accessType)) {
                if (accessCode == null || accessCode.isEmpty()) {
                    redirectAttributes.addFlashAttribute("error", "Za privatne tečajeve potreban je pristupni kod.");
                    return "redirect:/courses/add";
                }
            } else {
                accessCode = null;
            }
            

            Course course = new Course();
            course.setTitle(title);
            course.setDescription(description);
            course.setCoverPhoto(coverPhotoUrl);
            course.setAccessType(accessType);
            course.setAccessCode(accessCode);
            course.setInstructorId(instructorId);

            courseRepository.save(course);

            Forum forum = new Forum();
            forum.setCourseId(course.getCourseId());
            forum.setTitle("Forum za predmet: " + title);
            forum.setDescription(null);
        
        forumRepository.save(forum);

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

    @PostMapping("/{courseId}/addResources")
    public String addResources(@PathVariable("courseId") Long courseId,
                               @RequestParam("files") MultipartFile[] files,
                               @RequestParam("fileTitles") String[] fileTitles,
                            //    @RequestParam("links") String[] links,
                            //    @RequestParam("linkTitles") String[] linkTitles,
                               RedirectAttributes redirectAttributes) {
        try {
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                String title = fileTitles[i];
                if (!file.isEmpty()) {
                    String fileUrl = fileUploadService.uploadFile(file);
    
                    CourseResource courseResourceFile = new CourseResource();
                    courseResourceFile.setCourseId(courseId);
                    courseResourceFile.setTitle(title);
                    courseResourceFile.setType("pdf");
                    courseResourceFile.setLink(fileUrl);
                    courseRepository.saveCourseResources(courseResourceFile);
                }
            }

            // if (links != null) {
            //     for (int i = 0; i < links.length; i++) {
            //         String link = links[i];
            //         String title = linkTitles[i];
    
            //         if (!link.isEmpty()) {
            //             CourseResource courseResourceLink = new CourseResource();
            //             courseResourceLink.setCourseId(courseId);
            //             courseResourceLink.setTitle(title);
            //             courseResourceLink.setType("link");
            //             courseResourceLink.setLink(link);
            //             courseRepository.saveCourseResources(courseResourceLink);
            //         }
            //     }
            // }
    
            redirectAttributes.addFlashAttribute("success", "Dodatni resursi uspješno dodani!");

        } catch (IOException e) {

            redirectAttributes.addFlashAttribute("error", "Došlo je do pogreške pri uploadu resursa.");
            return "redirect:/courses/" + courseId;
        }
    
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

    @GetMapping("/{courseId}/forum")
    public String getCourseForumPage(@PathVariable("courseId") Long courseId, Model model) {

        Forum forum = forumRepository.getForumByCourseId(courseId);
        model.addAttribute("forum", forum);

        Course course = courseRepository.findById(courseId);
        model.addAttribute("course", course);
        
        List<ForumPost> forumPosts = forumRepository.getForumPostsByForumId(forum.getForumId());

        TimeAgoFormatter formatter = new TimeAgoFormatter();
        List<Map<String, Object>> formattedPosts = new ArrayList<>();
        for (ForumPost post : forumPosts) {

            UserProfile userProfile = userRepository.getUserProfileByUserId(post.getUserId());

            Map<String, Object> postMap = new HashMap<>();
            postMap.put("post", post);
            postMap.put("formattedTime", formatter.formatTimeAgo(post.getCreatedAt()));
            postMap.put("userProfile", userProfile);
            formattedPosts.add(postMap);
        }

        model.addAttribute("forumPosts", formattedPosts);

        return "courses/forum";
    }

    @PostMapping("/{courseId}/forum/createForumPost")
    public String createForumPost(@PathVariable("courseId") Long courseId, @ModelAttribute("forumPost") ForumPost forumPost, Model model) {

        Forum forum = forumRepository.getForumByCourseId(courseId);
        model.addAttribute("forum", forum);
        
        forumRepository.createForumPost(forumPost);

        return "courses/forum";
    }

    @GetMapping("/{courseId}/forum/{postId}")
    public String getCourseForumPostPage(@PathVariable("courseId") Long courseId, @PathVariable("postId") Long postId, Model model) {

        Forum forum = forumRepository.getForumByCourseId(courseId);
        model.addAttribute("forum", forum);

        Course course = courseRepository.findById(courseId);
        model.addAttribute("course", course);

        ForumPost forumPost = forumRepository.getForumPostByPostId(postId);
        model.addAttribute("forumPost", forumPost);

        List<ForumPostReply> postReplies = forumRepository.getForumPostRepliesByPostId(postId);

        TimeAgoFormatter formatter = new TimeAgoFormatter();
        List<Map<String, Object>> formattedPostReplies = new ArrayList<>();
        for (ForumPostReply postReply : postReplies) {

            UserProfile userProfile = userRepository.getUserProfileByUserId(postReply.getUserId());

            Map<String, Object> repliesMap = new HashMap<>();
            repliesMap.put("postReply", postReply);
            repliesMap.put("formattedTime", formatter.formatTimeAgo(postReply.getCreatedAt()));
            repliesMap.put("userProfile", userProfile);
            formattedPostReplies.add(repliesMap);
        }

        model.addAttribute("postReplies", formattedPostReplies);

        return "courses/forumPost";
    }
}