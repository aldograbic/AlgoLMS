package com.project.AlgoLMS.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.project.AlgoLMS.model.User;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public User findByUsername(String username) {
        String sql = "SELECT user_id, username, email, full_name, password, role, created_at, confirmation_token, email_verified FROM users WHERE username = ?";
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), username);
        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{email}, Integer.class);
        return count != null && count > 0;
    }

    @Override
    public void save(User user) {
        String sql = "INSERT INTO users (email, confirmation_token) VALUES (?, ?)";
        jdbcTemplate.update(sql, user.getEmail(), user.getConfirmationToken());
    }

    @Override
    public void saveFull(User user) {
        String sql = "UPDATE users SET username = ?, full_name = ?, password = ?";
        jdbcTemplate.update(sql, user.getUsername(), user.getFullName(), user.getPassword());
    }
}