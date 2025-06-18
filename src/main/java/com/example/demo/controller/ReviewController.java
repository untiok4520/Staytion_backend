package com.example.demo.controller;

import com.example.demo.dto.CreateReviewRequestDto;
import com.example.demo.dto.ReviewDto;
import com.example.demo.dto.ReviewResponseDto;
import com.example.demo.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    // 房型詳情頁：取得某飯店的評論
    @GetMapping("/rooms/{hotelId}/reviews")
    public List<ReviewDto> getRoomReviews(@PathVariable("hotelId") Long hotelId) {
        return reviewService.getByHotel(hotelId);
    }

    // 使用者中心：取得自己的評論
    @GetMapping("/users/{userId}/reviews")
    public List<ReviewDto> getMyReviews(@PathVariable("userId") Long userId) {
        return reviewService.getByUser(userId);
    }

    // 新增評論
    @PostMapping("/users/{userId}/reviews")
    public ReviewResponseDto postReview(@PathVariable("userId") Long userId,
                                        @Valid @RequestBody CreateReviewRequestDto req) {
        return reviewService.createReview(userId, req);
    }


    // 更新評論
    @PutMapping("/reviews/{id}")
    public ReviewDto putReview(@PathVariable("id") Long id,
                               @RequestBody ReviewDto dto) {
        return reviewService.update(id, dto);
    }

    // 刪除評論
    @DeleteMapping("/reviews/{id}")
    public void deleteReview(@PathVariable("id") Long id) {
        reviewService.delete(id);
    }
}
