package com.twitter.api.repository;

import com.twitter.api.entity.Like;
import com.twitter.api.entity.ReactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserIdAndTweetId(Long userId, Long tweetId);

    long countByTweetIdAndReactionType(Long tweetId, ReactionType reactionType);
}
