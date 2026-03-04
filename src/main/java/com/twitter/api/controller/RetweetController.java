package com.twitter.api.controller;

import com.twitter.api.dto.request.RetweetCreateRequest;
import com.twitter.api.dto.response.RetweetResponse;
import com.twitter.api.service.RetweetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/retweet")
@RequiredArgsConstructor
public class RetweetController {

    private final RetweetService retweetService;

    @PostMapping({"", "/"})
    public ResponseEntity<RetweetResponse> createRetweet(@Valid @RequestBody RetweetCreateRequest request) {
        RetweetResponse response = retweetService.createRetweet(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRetweet(@PathVariable Long id) {
        retweetService.deleteRetweet(id);
        return ResponseEntity.noContent().build();
    }
}
