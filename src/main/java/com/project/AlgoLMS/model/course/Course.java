package com.project.AlgoLMS.model.course;

import java.sql.Timestamp;

import com.project.AlgoLMS.model.user.User;

public class Course {
    private Long courseId;
    private String title;
    private String description;
    private String coverPhoto;
    private String accessCode;
    private String accessType;
    private Long instructorId;
    private Timestamp createdAt;

    private boolean isEnrolled;
    private User instructor;

    public Long getCourseId() {
        return courseId;
    }
    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getCoverPhoto() {
        return coverPhoto;
    }
    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto = coverPhoto;
    }
    public String getAccessCode() {
        return accessCode;
    }
    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }
    public String getAccessType() {
        return accessType;
    }
    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }
    public Long getInstructorId() {
        return instructorId;
    }
    public void setInstructorId(Long instructorId) {
        this.instructorId = instructorId;
    }
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    public boolean isEnrolled() {
        return isEnrolled;
    }
    public void setEnrolled(boolean isEnrolled) {
        this.isEnrolled = isEnrolled;
    }
    public User getInstructor() {
        return instructor;
    }
    public void setInstructor(User instructor) {
        this.instructor = instructor;
    }
}