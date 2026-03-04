package com.twitter.api.repository;

import com.twitter.api.entity.Retweet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RetweetRepository extends JpaRepository<Retweet, Long> {

    boolean existsByUserIdAndTweetId(Long userId, Long tweetId);

    long countByTweetId(Long tweetId);
}
