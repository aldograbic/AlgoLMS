package com.project.AlgoLMS.repository.enrollment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.project.AlgoLMS.model.enrollment.Enrollment;

@Repository
public class EnrollmentRepositoryImpl implements EnrollmentRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean existsByUserIdAndCourseId(Long userId, Long courseId) {
        String sql="SELECT COUNT(*) FROM enrollments WHERE user_id = ? AND course_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{userId, courseId}, Integer.class);
        return count != null && count > 0;
    }

    @Override
    public void save(Enrollment enrollment) {
        String sql="INSERT INTO enrollments (user_id, course_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, enrollment.getUserId(), enrollment.getCourseId());
    }
}