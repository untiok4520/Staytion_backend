package com.example.demo.projection;

import java.time.LocalDate;

public interface HistoryProjection {
	String getCname();

	String getImgUrl();

	LocalDate getCheckInDate();

	LocalDate getCheckOutDate();

	Integer getTotal();
}
