package com.project.AlgoLMS.repository.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.project.AlgoLMS.model.User;
import com.project.AlgoLMS.model.UserProfile;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public User findByEmail(String email) {
        String sql = "SELECT user_id, email, full_name, password, role, created_at, confirmation_token, email_verified FROM users WHERE email = ?";
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), email);
        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{email}, Integer.class);
        return count != null && count > 0;
    }

    @Override
    public User findByConfirmationToken(String token) {
        String sql = "SELECT user_id, email, full_name, password, role, created_at, confirmation_token, email_verified FROM users WHERE confirmation_token = ?";
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), token);
        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public void save(User user) {
        String sql = "INSERT INTO users (email, confirmation_token) VALUES (?, ?)";
        jdbcTemplate.update(sql, user.getEmail(), user.getConfirmationToken());
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE users SET email_verified = ?";
        jdbcTemplate.update(sql, user.isEmailVerified());
    }

    @Override
    public void saveFull(User user) {
        String sql = "UPDATE users SET full_name = ?, password = ?";
        jdbcTemplate.update(sql, user.getFullName(), user.getPassword());
    }

    @Override
    public void saveUserProfileDetails(UserProfile userProfile) {
        String sql = "INSERT INTO user_profiles (user_id, bio, profile_picture) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, userProfile.getUserId(), userProfile.getBio(), userProfile.getProfilePicture());
    }
}