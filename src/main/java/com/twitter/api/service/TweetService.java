package com.twitter.api.service;

import com.twitter.api.dto.request.TweetCreateRequest;
import com.twitter.api.dto.request.TweetUpdateRequest;
import com.twitter.api.dto.response.TweetResponse;

import java.util.List;

public interface TweetService {

    TweetResponse createTweet(TweetCreateRequest request);

    List<TweetResponse> findByUsername(String username);

    TweetResponse findById(Long tweetId);

    TweetResponse updateTweet(Long tweetId, TweetUpdateRequest request);

    void deleteTweet(Long tweetId);
}
