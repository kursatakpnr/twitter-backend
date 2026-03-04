package com.twitter.api.service;

import com.twitter.api.dto.request.RetweetCreateRequest;
import com.twitter.api.dto.response.RetweetResponse;

public interface RetweetService {

    RetweetResponse createRetweet(RetweetCreateRequest request);

    void deleteRetweet(Long retweetId);
}
