package com.project.AlgoLMS.repository.course;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.project.AlgoLMS.model.course.Course;
import com.project.AlgoLMS.model.course.CourseRowMapper;
import com.project.AlgoLMS.model.courseResource.CourseResource;
import com.project.AlgoLMS.repository.user.UserRepository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class CourseRepositoryImpl implements CourseRepository {

    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;

    public CourseRepositoryImpl(JdbcTemplate jdbcTemplate, UserRepository userRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRepository = userRepository;
    }

    @Override
    public Course findById(Long courseId) {
        String sql = "SELECT * FROM courses WHERE course_id = ?";
        return jdbcTemplate.queryForObject(sql, new CourseRowMapper(userRepository), courseId);
    }

    @Override
    public List<Course> getCourses() {
        String sql = "SELECT * FROM courses";
        return jdbcTemplate.query(sql, new CourseRowMapper(userRepository));
    }

    @Override
    public void save(Course course) {
        String sql = "INSERT INTO courses (title, description, cover_photo, access_code, access_type, instructor_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, course.getTitle());
            ps.setString(2, course.getDescription());
            ps.setString(3, course.getCoverPhoto());
            ps.setString(4, course.getAccessCode());
            ps.setString(5, course.getAccessType());
            ps.setLong(6, course.getInstructorId());
            return ps;
        }, keyHolder);

        course.setCourseId(keyHolder.getKey().longValue());
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
        return jdbcTemplate.query(sql, new CourseRowMapper(userRepository), userId);
    }

    @Override
    public void changeAccessCodeByCourseId(String accessCode, Long courseId) {
        String sql = "UPDATE courses SET access_code = ? WHERE course_id = ?";
        jdbcTemplate.update(sql, accessCode, courseId);
    }

    @Override
    public void saveCourseResources(CourseResource courseResource) {
        String sql = "INSERT INTO course_resources (course_id, title, type, link) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, courseResource.getCourseId(), courseResource.getTitle(), courseResource.getType(), courseResource.getLink());
    }
}