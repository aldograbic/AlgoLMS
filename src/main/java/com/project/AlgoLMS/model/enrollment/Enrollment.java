package com.project.AlgoLMS.model.enrollment;

import java.sql.Timestamp;

public class Enrollment {
    
    private Long enrollmentId;
    private Long userId;
    private Long courseId;
    private Timestamp enrolledAt;

    public Enrollment(Long userId, Long courseId) {
        this.userId = userId;
        this.courseId = courseId;
    }
    
    public Long getEnrollmentId() {
        return enrollmentId;
    }
    public void setEnrollmentId(Long enrollmentId) {
        this.enrollmentId = enrollmentId;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getCourseId() {
        return courseId;
    }
    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
    public Timestamp getEnrolledAt() {
        return enrolledAt;
    }
    public void setEnrolledAt(Timestamp enrolledAt) {
        this.enrolledAt = enrolledAt;
    }
}