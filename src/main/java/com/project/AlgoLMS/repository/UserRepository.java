package com.project.AlgoLMS.repository;

import com.project.AlgoLMS.model.User;

public interface UserRepository {
    
    User findByUsername(String username);
    boolean existsByEmail(String email);
    void save(User user);
    void saveFull(User user);
}
