package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.HotelDetailDTO;
import com.example.demo.dto.HotelSearchRequestDTO;
import com.example.demo.dto.HotelSearchResultDTO;
import com.example.demo.service.HotelService;

@RestController
@RequestMapping("/api/hotels")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @GetMapping
    public Map<String, Object> searchHotels(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String area,
            @RequestParam(required = false) String checkin,
            @RequestParam(required = false) String checkout,
            @RequestParam(required = false) Integer adult,
            @RequestParam(required = false) Integer child,
            @RequestParam(required = false) Integer room,
            @RequestParam(required = false) String price,
            @RequestParam(required = false) List<Long> amenity,
            @RequestParam(required = false) Integer score,
            @RequestParam(required = false) String sort,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        HotelSearchResultDTO resultDTO = new HotelSearchResultDTO();
        resultDTO.setCity(city);
        resultDTO.setArea(area);
        resultDTO.setCheckin(checkin);
        resultDTO.setCheckout(checkout);
        resultDTO.setAdult(adult);
        resultDTO.setChild(child);
        resultDTO.setRoom(room);
        resultDTO.setPrice(price);
        resultDTO.setAmenity(amenity);
        resultDTO.setScore(score);
        resultDTO.setSort(sort);

        Page<HotelSearchRequestDTO> hotelsPage = hotelService.searchHotels(resultDTO, page, size);
        Map<String, Object> rs = new HashMap<>();
        rs.put("hotels", hotelsPage.getContent());
        rs.put("total", hotelsPage.getTotalElements());
        rs.put("page", hotelsPage.getNumber() + 1);
        rs.put("size", hotelsPage.getSize());
        return rs;
    }

    //查詢符合條件飯店總數
    @GetMapping("/count")
    public Map<String, Long> getHotelCount(HotelSearchResultDTO resultDTO) {
        Long count = hotelService.searchHotels(resultDTO, 1, Integer.MAX_VALUE).getTotalElements();
        Map<String, Long> result = new HashMap<>();
        result.put("count", count);
        return result;
    }

    //飯店詳情
    @GetMapping("/{hotelId}")
    public HotelDetailDTO getHotelDetail(@PathVariable Long hotelId) {
        return hotelService.getHotelDetail(hotelId);
    }
}
