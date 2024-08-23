package com.project.AlgoLMS.repository.forum;

import java.util.List;

import com.project.AlgoLMS.model.forum.Forum;
import com.project.AlgoLMS.model.forum.ForumPost;
import com.project.AlgoLMS.model.forum.ForumPostReply;

public interface ForumRepository {

    void save(Forum forum);
    Forum getForumByCourseId(Long courseId);
    void createForumPost(ForumPost forumPost);
    List<ForumPost> getForumPostsByForumId(Long forumId);
    ForumPost getForumPostByPostId(Long postId);
    List<ForumPostReply> getForumPostRepliesByPostId(Long postId);
    void replyToForumPost(ForumPostReply forumPostReply);
}