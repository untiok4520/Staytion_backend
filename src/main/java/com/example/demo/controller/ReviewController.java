package com.example.demo.controller;

import com.example.demo.dto.CreateReviewRequestDto;
import com.example.demo.dto.ReviewDto;
import com.example.demo.dto.ReviewReplyDto;
import com.example.demo.dto.ReviewResponseDto;
import com.example.demo.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    // 房型詳情頁：取得某飯店的評論
    @GetMapping("/rooms/{hotelId}/reviews")
    @Operation(summary = "房型詳情頁：取得某飯店的評論")
    public List<ReviewResponseDto> getRoomReviews(@PathVariable("hotelId") Long hotelId) {
        return reviewService.getByHotel(hotelId);
    }

    // 使用者中心：取得自己的評論
    @GetMapping("/users/{userId}/reviews")
    @Operation(summary = "使用者中心：取得自己的評論")
    public List<ReviewResponseDto> getMyReviews(@PathVariable("userId") Long userId) {
        return reviewService.getByUser(userId);
    }
    //後台條件篩選分頁查詢
    @GetMapping("/host/reviews")
    @Operation(summary = "後台評論管理")
    public Page<ReviewResponseDto> searchReviewsForHost(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String comment,
            @RequestParam(required = false) String hotelName,
            @RequestParam(required = false) Integer minScore,
            @RequestParam(required = false) Integer maxScore,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Pageable pageable
    ){
        return reviewService.searchReviewsForHost(firstName, comment, hotelName, minScore, maxScore,startDate, endDate, pageable);
    }

    // 新增評論
    @PostMapping("/users/{userId}/reviews")
    @Operation(summary = "新增房客評論")
    public ReviewResponseDto postReview(@PathVariable("userId") Long userId,
                                        @Valid @RequestBody CreateReviewRequestDto req) {
        return reviewService.createReview(userId, req);
    }
    // 新增房東回覆
    @PatchMapping("/reviews/{orderId}/reply")
    @Operation(summary = "新增房東回覆")
    public ReviewResponseDto replyReview(
            @PathVariable("orderId") Long orderId,
            @RequestBody ReviewReplyDto dto
    ) {
        return reviewService.replyReview(orderId, dto.getReply());
    }

    // 更新評論
    @PutMapping("/users/{userId}/reviews/{id}")
    @Operation(summary = "更新評論")
    public ReviewResponseDto putReview(@PathVariable("userId") Long userId,
                                        @PathVariable("id") Long id,
                               @Valid @RequestBody CreateReviewRequestDto req) {
        return reviewService.updateReview(userId,id, req);
    }

    // 刪除評論
    @DeleteMapping("/users/{userId}/reviews/{id}")
    @Operation(summary = "刪除評論")
    public void deleteReview(
        @PathVariable("userId") Long userId,
        @PathVariable("id") Long id
    ) {
        reviewService.deleteReview(userId, id);
    }
}
