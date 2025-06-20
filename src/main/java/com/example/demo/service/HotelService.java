package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.projection.HotelProjection;
import com.example.demo.repository.HotelRepository;

@Service
public class HotelService {

	@Autowired
	private HotelRepository hotelRepository;
	
	// 查詢精選飯店資料
	public List<HotelProjection> getTopHotels() {
		return hotelRepository.findTopHotels();
	}
}
