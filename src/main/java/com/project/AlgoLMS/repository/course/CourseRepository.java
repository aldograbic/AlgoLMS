package com.project.AlgoLMS.repository.course;

import java.util.List;

import com.project.AlgoLMS.model.course.Course;
import com.project.AlgoLMS.model.courseResource.CourseResource;

public interface CourseRepository {
    
    Course findById(Long courseId);
    List<Course> getCourses();
    void save(Course course);
    void update(Course course);
    public List<Course> findCoursesByUserId(Long userId);
    void changeAccessCodeByCourseId(String accessCode, Long courseId);
    void saveCourseResources(CourseResource courseResource);
}