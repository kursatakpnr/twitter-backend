package com.twitter.api.controller;

import com.twitter.api.dto.request.LikeRequest;
import com.twitter.api.dto.response.LikeResponse;
import com.twitter.api.service.LikeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping({"/like", "/like/"})
    public ResponseEntity<LikeResponse> like(@Valid @RequestBody LikeRequest request) {
        LikeResponse response = likeService.like(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping({"/dislike", "/dislike/"})
    public ResponseEntity<LikeResponse> dislike(@Valid @RequestBody LikeRequest request) {
        LikeResponse response = likeService.dislike(request);
        return ResponseEntity.ok(response);
    }
}
