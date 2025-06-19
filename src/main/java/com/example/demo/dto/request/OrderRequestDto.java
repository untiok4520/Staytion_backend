package com.example.demo.dto.request;

import java.time.LocalDate;
import java.util.List;

public class OrderRequestDto {
	 private Long userId;
	    private LocalDate checkInDate;
	    private LocalDate checkOutDate;
	    private List<OrderItemRequestDto> items;
		public Long getUserId() {
			return userId;
		}
		public void setUserId(Long userId) {
			this.userId = userId;
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
		public List<OrderItemRequestDto> getItems() {
			return items;
		}
		public void setItems(List<OrderItemRequestDto> items) {
			this.items = items;
		}
	    
	    
}
