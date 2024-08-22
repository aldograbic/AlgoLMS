package com.project.AlgoLMS.repository.forum;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.project.AlgoLMS.model.forum.Forum;
import com.project.AlgoLMS.model.forum.ForumPost;
import com.project.AlgoLMS.model.forum.ForumPostRowMapper;
import com.project.AlgoLMS.repository.user.UserRepository;

@Repository
public class ForumRepositoryImpl implements ForumRepository{
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void save(Forum forum) {
        String sql = "INSERT INTO forums (course_id, title, description) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, forum.getCourseId(), forum.getTitle(), forum.getDescription());
    }

    @Override
    public Forum getForumByCourseId(Long courseId) {
        String sql = "SELECT * FROM forums WHERE course_id = ?";
        List<Forum> forums = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Forum.class), courseId);
        return forums.isEmpty() ? null : forums.get(0);
    }

    @Override
    public void createForumPost(ForumPost forumPost) {
        String sql = "INSERT INTO forum_posts (forum_id, user_id, title, content) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, forumPost.getForumId(), forumPost.getUserId(), forumPost.getTitle(), forumPost.getContent());
    }

    @Override
    public List<ForumPost> getForumPostsByForumId(Long forumId) {
        String sql = "SELECT * FROM forum_posts WHERE forum_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new ForumPostRowMapper(userRepository), forumId);
    }
}