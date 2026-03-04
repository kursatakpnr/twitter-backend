package com.twitter.api.service;

import com.twitter.api.dto.request.LikeRequest;
import com.twitter.api.dto.response.LikeResponse;

public interface LikeService {

    LikeResponse like(LikeRequest request);

    LikeResponse dislike(LikeRequest request);
}
