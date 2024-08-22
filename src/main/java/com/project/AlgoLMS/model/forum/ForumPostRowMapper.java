package com.project.AlgoLMS.model.forum;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.project.AlgoLMS.model.user.User;
import com.project.AlgoLMS.repository.user.UserRepository;

public class ForumPostRowMapper implements RowMapper<ForumPost> {

    private final UserRepository userRepository;

    public ForumPostRowMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ForumPost mapRow(ResultSet rs, int rowNum) throws SQLException {
        ForumPost forumPost = new ForumPost();
        forumPost.setPostId(rs.getLong("post_id"));
        forumPost.setForumId(rs.getLong("forum_id"));
        forumPost.setUserId(rs.getLong("user_id"));
        forumPost.setTitle(rs.getString("title"));
        forumPost.setContent(rs.getString("content"));
        forumPost.setCreatedAt(rs.getTimestamp("created_at"));

        Long userId = rs.getLong("user_id");
        User user = userRepository.findById(userId);
        forumPost.setUser(user);
        
        return forumPost;
    }
}
