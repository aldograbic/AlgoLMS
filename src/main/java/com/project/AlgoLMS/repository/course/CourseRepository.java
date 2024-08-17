package com.project.AlgoLMS.repository.course;

import java.util.List;

import com.project.AlgoLMS.model.course.Course;

public interface CourseRepository {
    
    Course findById(Long courseId);
    List<Course> getCourses();
    void save(Course course);
    void update(Course course);
}