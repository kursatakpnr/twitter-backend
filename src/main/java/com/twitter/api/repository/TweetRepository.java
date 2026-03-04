package com.twitter.api.repository;

import com.twitter.api.entity.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TweetRepository extends JpaRepository<Tweet, Long> {

    List<Tweet> findByAuthorIdOrderByCreatedAtDesc(Long authorId);

    List<Tweet> findByAuthorUsernameOrderByCreatedAtDesc(String username);
}
