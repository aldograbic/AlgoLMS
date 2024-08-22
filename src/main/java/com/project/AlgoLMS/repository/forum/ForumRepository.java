package com.project.AlgoLMS.repository.forum;

import java.util.List;

import com.project.AlgoLMS.model.forum.Forum;
import com.project.AlgoLMS.model.forum.ForumPost;

public interface ForumRepository {

    void save(Forum forum);
    Forum getForumByCourseId(Long courseId);
    void createForumPost(ForumPost forumPost);
    List<ForumPost> getForumPostsByForumId(Long forumId);
}