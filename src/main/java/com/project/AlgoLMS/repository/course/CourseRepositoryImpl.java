package com.project.AlgoLMS.repository.course;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.project.AlgoLMS.model.course.Course;
import com.project.AlgoLMS.model.course.CourseRowMapper;

import java.util.List;

@Repository
public class CourseRepositoryImpl implements CourseRepository {

    private final JdbcTemplate jdbcTemplate;

    public CourseRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Course findById(Long courseId) {
        String sql = "SELECT * FROM courses WHERE course_id = ?";
        return jdbcTemplate.queryForObject(sql, new CourseRowMapper(), courseId);
    }

    @Override
    public List<Course> getCourses() {
        String sql = "SELECT * FROM courses";
        return jdbcTemplate.query(sql, new CourseRowMapper());
    }

    @Override
    public void save(Course course) {
        String sql = "INSERT INTO courses (title, description, cover_photo, access_code, access_type, instructor_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                course.getTitle(),
                course.getDescription(),
                course.getCoverPhoto(),
                course.getAccessCode(),
                course.getAccessType(),
                course.getInstructorId()
        );
    }

    @Override
    public void update(Course course) {
        String sql = "UPDATE courses SET title = ?, description = ?, cover_photo = ?, access_code = ?, access_type = ?, instructor_id = ? " +
                     "WHERE course_id = ?";
        jdbcTemplate.update(sql,
                course.getTitle(),
                course.getDescription(),
                course.getCoverPhoto(),
                course.getAccessCode(),
                course.getAccessType(),
                course.getInstructorId(),
                course.getCourseId()
        );
    }

    @Override
    public List<Course> findCoursesByUserId(Long userId) {
        String sql = "SELECT c.* FROM courses c " +
                     "JOIN enrollments e ON c.course_id = e.course_id " +
                     "WHERE e.user_id = ?";
        return jdbcTemplate.query(sql, new CourseRowMapper(), userId);
    }

    @Override
    public void changeAccessCodeByCourseId(String accessCode, Long courseId) {
        String sql="UPDATE courses SET access_code = ? WHERE course_id = ?";
        jdbcTemplate.update(sql, accessCode, courseId);
    }
}