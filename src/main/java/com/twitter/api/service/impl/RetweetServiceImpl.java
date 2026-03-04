package com.twitter.api.service.impl;

import com.twitter.api.dto.request.RetweetCreateRequest;
import com.twitter.api.dto.response.RetweetResponse;
import com.twitter.api.entity.Retweet;
import com.twitter.api.entity.Tweet;
import com.twitter.api.entity.User;
import com.twitter.api.exception.ConflictException;
import com.twitter.api.exception.ResourceNotFoundException;
import com.twitter.api.exception.UnauthorizedException;
import com.twitter.api.mapper.EntityDtoMapper;
import com.twitter.api.repository.RetweetRepository;
import com.twitter.api.repository.TweetRepository;
import com.twitter.api.security.AuthenticatedUserProvider;
import com.twitter.api.service.RetweetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RetweetServiceImpl implements RetweetService {

    private final RetweetRepository retweetRepository;
    private final TweetRepository tweetRepository;
    private final AuthenticatedUserProvider authenticatedUserProvider;
    private final EntityDtoMapper entityDtoMapper;

    @Override
    @Transactional
    public RetweetResponse createRetweet(RetweetCreateRequest request) {
        User currentUser = authenticatedUserProvider.getCurrentUser();

        Tweet tweet = tweetRepository.findById(request.tweetId())
                .orElseThrow(() -> new ResourceNotFoundException("Tweet not found with id: " + request.tweetId()));

        if (retweetRepository.existsByUserIdAndTweetId(currentUser.getId(), tweet.getId())) {
            throw new ConflictException("You have already retweeted this tweet");
        }

        Retweet retweet = Retweet.builder()
                .user(currentUser)
                .tweet(tweet)
                .quoteText(normalize(request.quoteText()))
                .build();

        Retweet savedRetweet = retweetRepository.save(retweet);
        return entityDtoMapper.toRetweetResponse(savedRetweet);
    }

    @Override
    @Transactional
    public void deleteRetweet(Long retweetId) {
        User currentUser = authenticatedUserProvider.getCurrentUser();

        Retweet retweet = retweetRepository.findById(retweetId)
                .orElseThrow(() -> new ResourceNotFoundException("Retweet not found with id: " + retweetId));

        if (!retweet.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("Only the owner can delete this retweet");
        }

        retweetRepository.delete(retweet);
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
