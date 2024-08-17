package com.project.AlgoLMS.model.course;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class CourseRowMapper implements RowMapper<Course> {
    
    @Override
    public Course mapRow(ResultSet rs, int rowNum) throws SQLException {
        Course course = new Course();
        course.setCourseId(rs.getLong("course_id"));
        course.setTitle(rs.getString("title"));
        course.setDescription(rs.getString("description"));
        course.setCoverPhoto(rs.getString("cover_photo"));
        course.setAccessCode(rs.getString("access_code"));
        course.setAccessType(rs.getString("access_type"));
        course.setInstructorId(rs.getInt("instructor_id"));
        course.setCreatedAt(rs.getTimestamp("created_at"));
        return course;
    }
}