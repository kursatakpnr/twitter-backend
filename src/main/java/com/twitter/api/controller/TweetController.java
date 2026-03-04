package com.twitter.api.controller;

import com.twitter.api.dto.request.TweetCreateRequest;
import com.twitter.api.dto.request.TweetUpdateRequest;
import com.twitter.api.dto.response.TweetResponse;
import com.twitter.api.service.TweetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tweet")
@RequiredArgsConstructor
public class TweetController {

    private final TweetService tweetService;

    @PostMapping({"", "/"})
    public ResponseEntity<TweetResponse> createTweet(@Valid @RequestBody TweetCreateRequest request) {
        TweetResponse response = tweetService.createTweet(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/findByUsername")
    public ResponseEntity<List<TweetResponse>> findByUsername(@RequestParam String username) {
        List<TweetResponse> response = tweetService.findByUsername(username);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/findById")
    public ResponseEntity<TweetResponse> findById(@RequestParam Long id) {
        TweetResponse response = tweetService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TweetResponse> updateTweet(
            @PathVariable Long id,
            @Valid @RequestBody TweetUpdateRequest request
    ) {
        TweetResponse response = tweetService.updateTweet(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTweet(@PathVariable Long id) {
        tweetService.deleteTweet(id);
        return ResponseEntity.noContent().build();
    }
}
