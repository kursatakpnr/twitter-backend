package com.twitter.api.service.impl;

import com.twitter.api.dto.request.TweetCreateRequest;
import com.twitter.api.dto.request.TweetUpdateRequest;
import com.twitter.api.dto.response.TweetResponse;
import com.twitter.api.entity.ReactionType;
import com.twitter.api.entity.Tweet;
import com.twitter.api.entity.User;
import com.twitter.api.exception.ResourceNotFoundException;
import com.twitter.api.exception.UnauthorizedException;
import com.twitter.api.mapper.EntityDtoMapper;
import com.twitter.api.repository.CommentRepository;
import com.twitter.api.repository.LikeRepository;
import com.twitter.api.repository.RetweetRepository;
import com.twitter.api.repository.TweetRepository;
import com.twitter.api.repository.UserRepository;
import com.twitter.api.security.AuthenticatedUserProvider;
import com.twitter.api.service.TweetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final RetweetRepository retweetRepository;
    private final AuthenticatedUserProvider authenticatedUserProvider;
    private final EntityDtoMapper entityDtoMapper;

    @Override
    @Transactional
    public TweetResponse createTweet(TweetCreateRequest request) {
        User currentUser = authenticatedUserProvider.getCurrentUser();

        Tweet tweet = Tweet.builder()
                .content(request.content().trim())
                .mediaUrl(normalize(request.mediaUrl()))
                .author(currentUser)
                .build();

        Tweet savedTweet = tweetRepository.save(tweet);
        return toTweetResponse(savedTweet);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TweetResponse> findByUsername(String username) {
        String normalizedUsername = username == null ? null : username.trim();
        if (normalizedUsername == null || normalizedUsername.isEmpty()) {
            throw new ResourceNotFoundException("Username is required");
        }

        if (!userRepository.existsByUsername(normalizedUsername)) {
            throw new ResourceNotFoundException("User not found with username: " + normalizedUsername);
        }

        List<Tweet> tweets = tweetRepository.findByAuthorUsernameOrderByCreatedAtDesc(normalizedUsername);
        return tweets.stream().map(this::toTweetResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TweetResponse findById(Long tweetId) {
        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new ResourceNotFoundException("Tweet not found with id: " + tweetId));
        return toTweetResponse(tweet);
    }

    @Override
    @Transactional
    public TweetResponse updateTweet(Long tweetId, TweetUpdateRequest request) {
        User currentUser = authenticatedUserProvider.getCurrentUser();

        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new ResourceNotFoundException("Tweet not found with id: " + tweetId));

        validateTweetOwnership(currentUser, tweet);

        tweet.setContent(request.content().trim());
        tweet.setMediaUrl(normalize(request.mediaUrl()));

        Tweet updatedTweet = tweetRepository.save(tweet);
        return toTweetResponse(updatedTweet);
    }

    @Override
    @Transactional
    public void deleteTweet(Long tweetId) {
        User currentUser = authenticatedUserProvider.getCurrentUser();

        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new ResourceNotFoundException("Tweet not found with id: " + tweetId));

        validateTweetOwnership(currentUser, tweet);
        tweetRepository.delete(tweet);
    }

    private void validateTweetOwnership(User currentUser, Tweet tweet) {
        if (!tweet.getAuthor().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("Only the owner can modify or delete this tweet");
        }
    }

    private TweetResponse toTweetResponse(Tweet tweet) {
        long likeCount = likeRepository.countByTweetIdAndReactionType(tweet.getId(), ReactionType.LIKE);
        long dislikeCount = likeRepository.countByTweetIdAndReactionType(tweet.getId(), ReactionType.DISLIKE);
        long commentCount = commentRepository.countByTweetId(tweet.getId());
        long retweetCount = retweetRepository.countByTweetId(tweet.getId());
        return entityDtoMapper.toTweetResponse(tweet, likeCount, dislikeCount, commentCount, retweetCount);
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
