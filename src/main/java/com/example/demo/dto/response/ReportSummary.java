package com.example.demo.dto.response;

import java.util.List;

public class ReportSummary {
	private int totalRevenue;
	private int orderCount;
	private List<ChartData> roomTypeChart;
	private List<ChartData> trendChart;

	public ReportSummary(int totalRevenue, int orderCount, List<ChartData> roomTypeChart, List<ChartData> trendChart) {
		this.totalRevenue = totalRevenue;
		this.orderCount = orderCount;
		this.roomTypeChart = roomTypeChart;
		this.trendChart = trendChart;
	}

	// getter/setter

	public int getTotalRevenue() {
		return totalRevenue;
	}

	public void setTotalRevenue(int totalRevenue) {
		this.totalRevenue = totalRevenue;
	}

	public int getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}

	public List<ChartData> getRoomTypeChart() {
		return roomTypeChart;
	}

	public void setRoomTypeChart(List<ChartData> roomTypeChart) {
		this.roomTypeChart = roomTypeChart;
	}

	public List<ChartData> getTrendChart() {
		return trendChart;
	}

	public void setTrendChart(List<ChartData> trendChart) {
		this.trendChart = trendChart;
	}

}
