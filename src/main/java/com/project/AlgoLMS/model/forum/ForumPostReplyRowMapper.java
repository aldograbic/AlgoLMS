package com.project.AlgoLMS.model.forum;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.project.AlgoLMS.model.user.User;
import com.project.AlgoLMS.repository.user.UserRepository;

public class ForumPostReplyRowMapper implements RowMapper<ForumPostReply> {

    private final UserRepository userRepository;

    public ForumPostReplyRowMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ForumPostReply mapRow(ResultSet rs, int rowNum) throws SQLException {
        
        ForumPostReply forumPostReply = new ForumPostReply();
        forumPostReply.setReplyId(rs.getLong("reply_id"));
        forumPostReply.setPostId(rs.getLong("post_id"));
        forumPostReply.setUserId(rs.getLong("user_id"));
        forumPostReply.setContent(rs.getString("content"));
        forumPostReply.setCreatedAt(rs.getTimestamp("created_at"));

        Long userId = rs.getLong("user_id");
        User user = userRepository.findById(userId);
        forumPostReply.setUser(user);

        return forumPostReply;
    }
}