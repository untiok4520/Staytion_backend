package com.example.demo.service;

import com.example.demo.dto.ReviewDto;
import com.example.demo.entity.Review;
import com.example.demo.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepo;

    public List<ReviewDto> getByHotel(Long hotelId){
        return reviewRepo.findByHotelId(hotelId).stream().map(this::toDto).collect(Collectors.toList());
    }

    // 取得使用者中心的評論紀錄
    public List<ReviewDto> getByUser(Long userId) {
        return reviewRepo.findByUserId(userId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // 新增評論
    public ReviewDto create(ReviewDto dto) {
        if (reviewRepo.existsById(dto.getOrderId())) {
            throw new RuntimeException("該訂單已經有評論了");
        }

        Review r = new Review();
        r.setOrderId(dto.getOrderId());
        r.setScore(dto.getScore());
        r.setComment(dto.getComment());
        // 如果你需要設定 user 與 hotel 關聯，也可以在 dto 裡帶入對應 id，
        // 然後透過 UserRepository / HotelRepository 查出實體再 setUser(…)、setHotel(…)
        Review saved = reviewRepo.save(r);
        return toDto(saved);
    }

    // 更新評論
    public ReviewDto update(Long reviewId, ReviewDto dto) {
        Review r = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("評論不存在"));
        r.setScore(dto.getScore());
        r.setComment(dto.getComment());
        r.setReply(dto.getReply());
        Review updated = reviewRepo.save(r);
        return toDto(updated);
    }

    // 刪除評論
    public void delete(Long reviewId) {
        reviewRepo.deleteById(reviewId);
    }

    // Entity → DTO 轉換
    private ReviewDto toDto(Review r) {
        ReviewDto dto = new ReviewDto();
        dto.setOrderId(r.getOrderId());
        dto.setScore(r.getScore());
        dto.setComment(r.getComment());
        dto.setReply(r.getReply());
        if (r.getUser() != null) {
            dto.setUserName(r.getUser().getFirstName());
        }
        dto.setCreatedAt(r.getCreatedAt());
        return dto;
    }
}
