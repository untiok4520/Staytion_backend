package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.response.ChartData;
import com.example.demo.dto.response.ReportSummary;
import com.example.demo.repository.OrderRepository;

@Service
public class StatisticsService {

	@Autowired
	private OrderRepository orderRepository;

	/**
	 * 取得該業主所有有訂單的年份（依 checkInDate）
	 */
	public List<Integer> getAvailableYears(Long ownerId) {
		return orderRepository.findAvailableYears(ownerId);
	}

	/**
	 * 取得該業主指定年份有訂單的月份（依 checkInDate）
	 */
	public List<Integer> getAvailableMonths(Long ownerId, int year) {
		return orderRepository.findAvailableMonths(ownerId, year);
	}

	/**
	 * 取得該業主指定年月的統計報表摘要（總收入、訂單數、房型統計、趨勢圖）
	 */
	public ReportSummary getReportSummary(Long ownerId, int year, int month) {
		int revenue = orderRepository.sumRevenue(ownerId, year, month);
		int orderCount = orderRepository.countOrders(ownerId, year, month);

		List<Map<String, Object>> roomTypeChartRaw = orderRepository.getRoomTypeChart(ownerId, year, month);
		List<Map<String, Object>> trendChartRaw = orderRepository.getTrendChart(ownerId, year, month);

		// 轉換房型統計資料
		List<ChartData> roomTypeChart = roomTypeChartRaw.stream()
				.map(row -> new ChartData((String) row.get("label"), ((Number) row.get("value")).intValue()))
				.collect(Collectors.toList());

		// 轉換每日收入趨勢資料
		List<ChartData> trendChart = trendChartRaw.stream()
				.map(row -> new ChartData(String.valueOf(row.get("label")), ((Number) row.get("value")).intValue()))
				.collect(Collectors.toList());

		return new ReportSummary(revenue, orderCount, roomTypeChart, trendChart);
	}

	/**
	 * 取得該業主某年份的每月總收入
	 */
	public List<Map<String, Object>> getMonthlyRevenue(Long ownerId, int year) {
		return orderRepository.getMonthlyRevenue(ownerId, year);
	}

	/**
	 * 取得該業主在指定時間區間內的每日訂單趨勢（含補零）
	 */
	public List<Map<String, Object>> getOrderTrend(Long ownerId, LocalDate start, LocalDate end) {
		LocalDateTime startDateTime = start.atStartOfDay();
		LocalDateTime endDateTime = end.plusDays(1).atStartOfDay(); // 包含整天

		List<Map<String, Object>> rawData = orderRepository.getOrderTrend(ownerId, startDateTime, endDateTime);

		// 將查詢結果轉為 Map<LocalDate, Long>
		Map<LocalDate, Long> trendMap = new HashMap<>();
		for (Map<String, Object> row : rawData) {
			LocalDate date = ((java.sql.Date) row.get("date")).toLocalDate();
			Long count = (Long) row.get("orderCount");
			trendMap.put(date, count);
		}

		// 建立完整日期區間並補 0
		List<Map<String, Object>> result = new ArrayList<>();
		LocalDate current = start;
		while (!current.isAfter(end)) {
			Long count = trendMap.getOrDefault(current, 0L);
			Map<String, Object> entry = new HashMap<>();
			entry.put("date", current);
			entry.put("orderCount", count);
			result.add(entry);
			current = current.plusDays(1);
		}

		return result;
	}
}
