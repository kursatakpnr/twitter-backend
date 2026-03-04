package com.twitter.api.repository;

import com.twitter.api.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    long countByTweetId(Long tweetId);

    List<Comment> findByTweetIdOrderByCreatedAtAsc(Long tweetId);
}
