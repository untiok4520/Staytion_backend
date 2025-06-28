package com.example.demo.repository;

import com.example.demo.dto.FavHotelDto;
import com.example.demo.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    void deleteByUserIdAndHotelId(Long userId, Long hotelId);

    boolean existsByUserIdAndHotelId(Long userId, Long hotelId);

    @Query("""
                SELECT new com.example.demo.dto.FavHotelDto(
                    h.id,
                    h.hname,
                    c.cname,
                    d.dname,
                    CAST(COALESCE(ROUND(AVG(rv.score), 1),0) AS double),
                    CAST(COALESCE(MIN(rt.price), 0) AS double),
                    COUNT(rv),
                    img.imgUrl
                )
                FROM Favorite f
                JOIN f.hotel h
                JOIN h.district d
                JOIN d.city c
                LEFT JOIN h.images img ON img.isCover = true
                LEFT JOIN h.reviews rv
                LEFT JOIN h.roomTypes rt
                WHERE f.userId = :userId
                  AND (:city IS NULL OR c.cname LIKE CONCAT('%', :city, '%'))
                GROUP BY
                    h.id, h.hname, c.cname, d.dname, img.imgUrl
            """)
    List<FavHotelDto> findFavoritesDtoByUserIdAndCity(
            @Param("userId") Long userId,
            @Param("city") String city
    );
}
