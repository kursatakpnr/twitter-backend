package com.twitter.api.service.impl;

import com.twitter.api.dto.request.LikeRequest;
import com.twitter.api.dto.response.LikeResponse;
import com.twitter.api.entity.Like;
import com.twitter.api.entity.ReactionType;
import com.twitter.api.entity.Tweet;
import com.twitter.api.entity.User;
import com.twitter.api.exception.ResourceNotFoundException;
import com.twitter.api.mapper.EntityDtoMapper;
import com.twitter.api.repository.LikeRepository;
import com.twitter.api.repository.TweetRepository;
import com.twitter.api.security.AuthenticatedUserProvider;
import com.twitter.api.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final TweetRepository tweetRepository;
    private final AuthenticatedUserProvider authenticatedUserProvider;
    private final EntityDtoMapper entityDtoMapper;

    @Override
    @Transactional
    public LikeResponse like(LikeRequest request) {
        if (request.reactionType() != ReactionType.LIKE) {
            throw new IllegalArgumentException("For /like endpoint reactionType must be LIKE");
        }
        return upsertReaction(request.tweetId(), ReactionType.LIKE);
    }

    @Override
    @Transactional
    public LikeResponse dislike(LikeRequest request) {
        if (request.reactionType() != ReactionType.DISLIKE) {
            throw new IllegalArgumentException("For /dislike endpoint reactionType must be DISLIKE");
        }
        return upsertReaction(request.tweetId(), ReactionType.DISLIKE);
    }

    private LikeResponse upsertReaction(Long tweetId, ReactionType reactionType) {
        User currentUser = authenticatedUserProvider.getCurrentUser();

        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new ResourceNotFoundException("Tweet not found with id: " + tweetId));

        Like like = likeRepository.findByUserIdAndTweetId(currentUser.getId(), tweet.getId())
                .orElseGet(() -> Like.builder()
                        .user(currentUser)
                        .tweet(tweet)
                        .reactionType(reactionType)
                        .build());

        like.setReactionType(reactionType);
        Like savedLike = likeRepository.save(like);
        return entityDtoMapper.toLikeResponse(savedLike);
    }
}
