package com.example.demo.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class HistoryRequestDto {
	
	private Long userId;
    private String cityName;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer Adults;
    private Integer Kids;
    private LocalDateTime searchTime;

   
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public LocalDate getCheckInDate() {
		return checkInDate;
	}
	public void setCheckInDate(LocalDate checkInDate) {
		this.checkInDate = checkInDate;
	}
	public LocalDate getCheckOutDate() {
		return checkOutDate;
	}
	public void setCheckOutDate(LocalDate checkOutDate) {
		this.checkOutDate = checkOutDate;
	}
	
	public Integer getAdults() {
		return Adults;
	}
	public void setAdults(Integer adults) {
		Adults = adults;
	}
	public Integer getKids() {
		return Kids;
	}
	public void setKids(Integer kids) {
		Kids = kids;	
	}
	
	public LocalDateTime getSearchTime() {
		return searchTime;
	}
	public void setSearchTime(LocalDateTime searchTime) {
		this.searchTime = searchTime;
	}

	
	
}
