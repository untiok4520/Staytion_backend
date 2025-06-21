package com.example.demo.controller;

import com.example.demo.dto.FavHotelDto;
import com.example.demo.dto.FavoriteDto;
import com.example.demo.entity.Favorite;
import com.example.demo.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;
    public FavoriteController(FavoriteService favoriteService){
        this.favoriteService = favoriteService;
    }
    // 加入收藏
    @PostMapping
    @Operation(summary = "加入收藏")
    public ResponseEntity<Favorite> addFavorite(@RequestBody FavoriteDto dto) {
        return ResponseEntity.ok(favoriteService.addFavorite(dto));
    }

    // 移除收藏
    @DeleteMapping
    @Operation(summary = "移除收藏")
    public ResponseEntity<Void> removeFavorite(@RequestParam Long userId, @RequestParam Long hotelId) {
        favoriteService.removeFavorite(userId, hotelId);
        return ResponseEntity.noContent().build();
    }

    // 是否已收藏
    @GetMapping("/check")
    @Operation(summary = "是否已收藏")
    public ResponseEntity<Boolean> isFavorite(@RequestParam Long userId, @RequestParam Long hotelId) {
        return ResponseEntity.ok(favoriteService.isFavorite(userId, hotelId));
    }

    // 查詢使用者的收藏清單，可依城市篩選
    @GetMapping("/users/{userId}")
    @Operation(summary = "查詢使用者收藏清單，可依城市篩選")
    public ResponseEntity<List<FavHotelDto>> getFavorites(@PathVariable Long userId,
                                                          @RequestParam(required = false) String city) {
        return ResponseEntity.ok(favoriteService.getFavoriteHotels(userId, city));
    }
}
