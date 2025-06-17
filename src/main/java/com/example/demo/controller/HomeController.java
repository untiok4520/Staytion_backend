package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.projection.HistoryProjection;
import com.example.demo.projection.HotelProjection;
import com.example.demo.service.CityService;
import com.example.demo.service.HistoryService;
import com.example.demo.service.HotelService;

@RestController
@RequestMapping("/api")
public class HomeController {

	@Autowired
	private HistoryService historyService;

	@Autowired
	private HotelService hotelService;

	@Autowired
	private CityService cityService;

	// 查詢搜尋紀錄
	@GetMapping("/histories/{userId}")
	public List<HistoryProjection> getHistoriesByUserId(@PathVariable Long userId) {
		return historyService.getHistoriesByUserId(userId);
	}

	// 查詢精選飯店
	@GetMapping("/top-hotels")
	public List<HotelProjection> getTopHotels() {
		return hotelService.getTopHotels();
	}

	// 查詢熱門城市資料
	@GetMapping("/city/{id}/hotel-count")
	public ResponseEntity<Map<String, Object>> getCityHotelCount(@PathVariable("id") Long id) {
		return ResponseEntity.ok(cityService.getCityHotelCount(id));
	}

}
