package com.project.AlgoLMS.repository.forum;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.project.AlgoLMS.model.forum.Forum;
import com.project.AlgoLMS.model.forum.ForumPost;
import com.project.AlgoLMS.model.forum.ForumPostReply;
import com.project.AlgoLMS.model.forum.ForumPostReplyRowMapper;
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

    @Override
    public ForumPost getForumPostByPostId(Long postId) {
        String sql = "SELECT * FROM forum_posts WHERE post_id = ?";
        List<ForumPost> forumPosts = jdbcTemplate.query(sql, new ForumPostRowMapper(userRepository), postId);
        return forumPosts.isEmpty() ? null : forumPosts.get(0);
    }

    @Override
    public List<ForumPostReply> getForumPostRepliesByPostId(Long postId) {
        String sql = "SELECT * FROM forum_post_replies WHERE post_id = ? ORDER BY created_at ASC";
        return jdbcTemplate.query(sql, new ForumPostReplyRowMapper(userRepository), postId);
    }

    @Override
    public void replyToForumPost(ForumPostReply forumPostReply) {
        String sql = "INSERT INTO forum_post_replies (post_id, user_id, content) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, forumPostReply.getPostId(), forumPostReply.getUserId(), forumPostReply.getContent());
    }
}