package com.project.AlgoLMS.repository.enrollment;

import com.project.AlgoLMS.model.enrollment.Enrollment;

public interface EnrollmentRepository {

    boolean existsByUserIdAndCourseId(Long userId, Long courseId);
    void save(Enrollment enrollment);
}
