package com.project.AlgoLMS.model.userProfile;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class UserProfileRowMapper implements RowMapper<UserProfile> {

    @Override
    public UserProfile mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserProfile userProfile = new UserProfile();
        userProfile.setProfileId(rs.getLong("profile_id"));
        userProfile.setUserId(rs.getLong("user_id"));
        userProfile.setBio(rs.getString("bio"));
        userProfile.setProfilePicture(rs.getString("profile_picture"));

        return userProfile;
    }
}