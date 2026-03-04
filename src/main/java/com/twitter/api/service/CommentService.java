package com.twitter.api.service;

import com.twitter.api.dto.request.CommentCreateRequest;
import com.twitter.api.dto.request.CommentUpdateRequest;
import com.twitter.api.dto.response.CommentResponse;

import java.util.List;

public interface CommentService {

    CommentResponse createComment(CommentCreateRequest request);

    List<CommentResponse> findByTweetId(Long tweetId);

    CommentResponse updateComment(Long commentId, CommentUpdateRequest request);

    void deleteComment(Long commentId);
}
