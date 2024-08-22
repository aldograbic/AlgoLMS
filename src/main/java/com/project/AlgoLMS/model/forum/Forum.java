package com.project.AlgoLMS.model.forum;

import java.sql.Timestamp;

public class Forum {
    
    private Long forumId;
    private Long courseId;
    private String title;
    private String description;
    private Timestamp createdAt;

    public Long getForumId() {
        return forumId;
    }
    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }
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
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}