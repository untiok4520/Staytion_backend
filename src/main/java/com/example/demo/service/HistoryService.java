package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.projection.HistoryProjection;
import com.example.demo.repository.HistoryRepository;

@Service
public class HistoryService {
	
	@Autowired
    private HistoryRepository historyRepository;

    public List<HistoryProjection> getHistoriesByUserId(Long userId) {
        return historyRepository.findHistoriesByUserId(userId);
    }
}
