package com.project.AlgoLMS.repository;

import com.project.AlgoLMS.model.User;

public interface UserRepository {
    
    User findByUsername(String username);
    void save(User user);
}
