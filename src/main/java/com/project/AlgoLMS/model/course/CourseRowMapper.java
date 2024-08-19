package com.project.AlgoLMS.model.course;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

import com.project.AlgoLMS.model.user.User;
import com.project.AlgoLMS.repository.user.UserRepository;

public class CourseRowMapper implements RowMapper<Course> {

    private final UserRepository userRepository;

    public CourseRowMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public Course mapRow(ResultSet rs, int rowNum) throws SQLException {
        Course course = new Course();
        course.setCourseId(rs.getLong("course_id"));
        course.setTitle(rs.getString("title"));
        course.setDescription(rs.getString("description"));
        course.setCoverPhoto(rs.getString("cover_photo"));
        course.setAccessCode(rs.getString("access_code"));
        course.setAccessType(rs.getString("access_type"));
        course.setInstructorId(rs.getLong("instructor_id"));
        course.setCreatedAt(rs.getTimestamp("created_at"));

        Long instructorId = rs.getLong("instructor_id");
        User instructor = userRepository.findById(instructorId);
        course.setInstructor(instructor);

        return course;
    }
}