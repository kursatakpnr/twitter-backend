package com.twitter.api.mapper;

import com.twitter.api.dto.response.CommentResponse;
import com.twitter.api.dto.response.LikeResponse;
import com.twitter.api.dto.response.RetweetResponse;
import com.twitter.api.dto.response.TweetResponse;
import com.twitter.api.dto.response.UserSummaryResponse;
import com.twitter.api.entity.Comment;
import com.twitter.api.entity.Like;
import com.twitter.api.entity.Retweet;
import com.twitter.api.entity.Tweet;
import com.twitter.api.entity.User;
import org.springframework.stereotype.Component;

@Component
public class EntityDtoMapper {

    public UserSummaryResponse toUserSummary(User user) {
        return new UserSummaryResponse(
                user.getId(),
                user.getUsername(),
                user.getDisplayName(),
                user.getProfileImageUrl()
        );
    }

    public TweetResponse toTweetResponse(
            Tweet tweet,
            long likeCount,
            long dislikeCount,
            long commentCount,
            long retweetCount
    ) {
        return new TweetResponse(
                tweet.getId(),
                tweet.getContent(),
                tweet.getMediaUrl(),
                toUserSummary(tweet.getAuthor()),
                likeCount,
                dislikeCount,
                commentCount,
                retweetCount,
                tweet.getCreatedAt(),
                tweet.getUpdatedAt()
        );
    }

    public CommentResponse toCommentResponse(Comment comment) {
        Long parentId = comment.getParentComment() == null ? null : comment.getParentComment().getId();
        return new CommentResponse(
                comment.getId(),
                comment.getTweet().getId(),
                parentId,
                comment.getContent(),
                toUserSummary(comment.getAuthor()),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }

    public LikeResponse toLikeResponse(Like like) {
        return new LikeResponse(
                like.getId(),
                like.getTweet().getId(),
                like.getUser().getId(),
                like.getReactionType(),
                like.getCreatedAt(),
                like.getUpdatedAt()
        );
    }

    public RetweetResponse toRetweetResponse(Retweet retweet) {
        return new RetweetResponse(
                retweet.getId(),
                retweet.getTweet().getId(),
                retweet.getUser().getId(),
                retweet.getQuoteText(),
                retweet.getCreatedAt(),
                retweet.getUpdatedAt()
        );
    }
}
