package com.project.AlgoLMS.model.forum;

import java.sql.Timestamp;

import com.project.AlgoLMS.model.user.User;

public class ForumPost {
    
    private Long postId;
    private Long forumId;
    private Long userId;
    private String title;
    private String content;
    private Timestamp createdAt;

    private User user;

    public Long getPostId() {
        return postId;
    }
    public void setPostId(Long postId) {
        this.postId = postId;
    }
    public Long getForumId() {
        return forumId;
    }
    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
}