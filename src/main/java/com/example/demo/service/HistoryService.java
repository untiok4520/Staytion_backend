package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.HistoryRequestDto;
import com.example.demo.dto.HistoryResponseDto;
import com.example.demo.entity.City;
import com.example.demo.entity.History;
import com.example.demo.entity.User;
import com.example.demo.projection.HistoryProjection;
import com.example.demo.repository.CityRepository;
import com.example.demo.repository.HistoryRepository;
import com.example.demo.repository.UserRepository;

@Service
public class HistoryService {

	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CityRepository cityRepository;

	public List<HistoryProjection> getHistoriesByUserId(Long userId) {
		return historyRepository.findHistoriesByUserId(userId);
	}

	public HistoryResponseDto saveHistory(HistoryRequestDto dto) {
		User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));

		City city = cityRepository.findByCname(dto.getCityName())
				.orElseThrow(() -> new RuntimeException("City not found"));

		History history = new History();
		history.setCheckInDate(dto.getCheckInDate());
		history.setCheckOutDate(dto.getCheckOutDate());
		history.setAdults(dto.getAdults());
		history.setKids(dto.getKids());
		history.setSearchTime(dto.getSearchTime() != null ? dto.getSearchTime() : LocalDateTime.now());
		history.setUser(user);
		history.setCity(city);

		History saved = historyRepository.save(history);

		HistoryResponseDto response = new HistoryResponseDto();
		response.setCityName(city.getCname());
		response.setCityImageUrl(city.getImgUrl());
		response.setCheckInDate(saved.getCheckInDate());
		response.setCheckOutDate(saved.getCheckOutDate());
		response.setTotal(saved.getAdults() + saved.getKids());
		response.setSearchTime(saved.getSearchTime());

		return response;
	}
}
