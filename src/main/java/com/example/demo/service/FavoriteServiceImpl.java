package com.example.demo.service;

import com.example.demo.dto.FavHotelDto;
import com.example.demo.dto.FavoriteDto;
import com.example.demo.entity.Favorite;
import com.example.demo.entity.Hotel;
import com.example.demo.entity.Image;
import com.example.demo.repository.FavoriteRepository;
import com.example.demo.repository.HotelRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService{
    private final FavoriteRepository favoriteRepository;
    private final HotelRepository hotelRepository;

    public FavoriteServiceImpl(FavoriteRepository favoriteRepository, HotelRepository hotelRepository){
        this.favoriteRepository = favoriteRepository;
        this.hotelRepository = hotelRepository;
    }

    //加入收藏清單
    @Override
    public Favorite addFavorite(FavoriteDto dto) {
        Favorite favorite = new Favorite(dto.getUserId(), dto.getHotelId());
        return favoriteRepository.save(favorite);
    }

    //移除收藏清單
    @Override
    @Transactional
    public void removeFavorite(Long userId, Long hotelId) {
        favoriteRepository.deleteByUserIdAndHotelId(userId, hotelId);

    }

    //是否是收藏飯店（判斷愛心）
    @Override
    public boolean isFavorite(Long userId, Long hotelId) {
        return favoriteRepository.existsByUserIdAndHotelId(userId, hotelId);
    }

    //用城市查詢篩選收藏清單
    @Override
    public List<FavHotelDto> getFavoriteHotels(Long userId, String city) {
        List<Hotel> hotels = favoriteRepository.findFavoriteHotelsByUserIdAndCity(userId, city);

        return hotels.stream()
                .map(h -> {
                    String coverUrl = h.getImages().stream()
                            .filter(Image::getIsCover)
                            .map(Image::getImgUrl)
                            .findFirst()
                            .orElse(null);
                    return new FavHotelDto(
                            h.getId(),
                            h.getHname(),
                            h.getDistrict().getCity().getCname(),
                            coverUrl
                    );
                })
                .toList();
    }
}
