package com.twitter.api.service.impl;

import com.twitter.api.dto.request.CommentCreateRequest;
import com.twitter.api.dto.request.CommentUpdateRequest;
import com.twitter.api.dto.response.CommentResponse;
import com.twitter.api.entity.Comment;
import com.twitter.api.entity.Tweet;
import com.twitter.api.entity.User;
import com.twitter.api.exception.ConflictException;
import com.twitter.api.exception.ResourceNotFoundException;
import com.twitter.api.exception.UnauthorizedException;
import com.twitter.api.mapper.EntityDtoMapper;
import com.twitter.api.repository.CommentRepository;
import com.twitter.api.repository.TweetRepository;
import com.twitter.api.security.AuthenticatedUserProvider;
import com.twitter.api.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TweetRepository tweetRepository;
    private final AuthenticatedUserProvider authenticatedUserProvider;
    private final EntityDtoMapper entityDtoMapper;

    @Override
    @Transactional
    public CommentResponse createComment(CommentCreateRequest request) {
        User currentUser = authenticatedUserProvider.getCurrentUser();

        Tweet tweet = tweetRepository.findById(request.tweetId())
                .orElseThrow(() -> new ResourceNotFoundException("Tweet not found with id: " + request.tweetId()));

        Comment parentComment = null;
        if (request.parentCommentId() != null) {
            parentComment = commentRepository.findById(request.parentCommentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent comment not found with id: " + request.parentCommentId()));

            if (!parentComment.getTweet().getId().equals(tweet.getId())) {
                throw new ConflictException("Parent comment must belong to the same tweet");
            }
        }

        Comment comment = Comment.builder()
                .content(request.content().trim())
                .tweet(tweet)
                .author(currentUser)
                .parentComment(parentComment)
                .build();

        Comment savedComment = commentRepository.save(comment);
        return entityDtoMapper.toCommentResponse(savedComment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponse> findByTweetId(Long tweetId) {
        if (!tweetRepository.existsById(tweetId)) {
            throw new ResourceNotFoundException("Tweet not found with id: " + tweetId);
        }

        return commentRepository.findByTweetIdOrderByCreatedAtAsc(tweetId)
                .stream()
                .map(entityDtoMapper::toCommentResponse)
                .toList();
    }

    @Override
    @Transactional
    public CommentResponse updateComment(Long commentId, CommentUpdateRequest request) {
        User currentUser = authenticatedUserProvider.getCurrentUser();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));

        validateCommentOwnership(currentUser, comment);

        comment.setContent(request.content().trim());
        Comment updatedComment = commentRepository.save(comment);
        return entityDtoMapper.toCommentResponse(updatedComment);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        User currentUser = authenticatedUserProvider.getCurrentUser();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));

        validateCommentOwnership(currentUser, comment);
        commentRepository.delete(comment);
    }

    private void validateCommentOwnership(User currentUser, Comment comment) {
        if (!comment.getAuthor().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("Only the owner can modify or delete this comment");
        }
    }
}
