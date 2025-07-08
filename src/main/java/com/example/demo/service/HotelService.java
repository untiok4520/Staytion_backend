package com.example.demo.service;

import com.example.demo.dto.HotelDetailDTO;
import com.example.demo.dto.HotelSearchRequest;
import com.example.demo.dto.HotelSearchResult;
import com.example.demo.projection.HotelProjection;
import org.springframework.data.domain.Page;

import java.util.List;

public interface HotelService {
    List<HotelProjection> getTopHotels();

    Page<HotelSearchRequest> searchHotels(HotelSearchResult dto, int page, int size);

    HotelDetailDTO getHotelDetail(Long hotelId);

    List<HotelSearchRequest> searchHotelsByName(String keyword, Long highlightHotelId);

    Long getHotelOwnerId(Long hotelId);
}
