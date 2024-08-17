package com.project.AlgoLMS.repository.user;

import com.project.AlgoLMS.model.user.User;
import com.project.AlgoLMS.model.userProfile.UserProfile;

public interface UserRepository {
    
    User findByEmail(String email);
    boolean existsByEmail(String email);
    User findByConfirmationToken(String token);
    void save(User user);
    void update(User user);
    void saveFull(User user);
    void saveUserProfileDetails(UserProfile userProfile);
    User findById(Long userId);
    UserProfile getUserProfileByUserId(Long userId);
}