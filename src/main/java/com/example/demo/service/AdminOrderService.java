package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.response.OrderResponseDto;
import com.example.demo.entity.Order;
import com.example.demo.entity.Payment;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.repository.OrderRepository;

@Service
public class AdminOrderService {

	@Autowired
	private OrderRepository orderRepository;

	// 查全部訂單
	public List<OrderResponseDto> getAllOrders() {
		return orderRepository.findAll().stream().map(OrderMapper::toDto).toList();
	}

	// 查某飯店所有訂單（後台）
	public List<OrderResponseDto> getOrdersByHotelId(Long hotelId) {
		return orderRepository.findByRoomTypeHotelId(hotelId).stream().map(OrderMapper::toDto).toList();
	}

	// 狀態 + 日期篩選 + 分頁
	public Page<OrderResponseDto> searchOrders(Long currentUserId, Order.OrderStatus status, LocalDate start,
			LocalDate end, String keyword, Payment.PaymentMethod paymentMethod, Payment.PaymentStatus paymentStatus,
			Pageable pageable) {
		LocalDateTime startDateTime = (start != null) ? start.atStartOfDay() : null;
		LocalDateTime endDateTime = (end != null) ? end.plusDays(1).atStartOfDay() : null;

		return orderRepository.searchAccessibleOrdersWithKeyword(currentUserId, status, startDateTime, endDateTime,
				keyword, paymentMethod, paymentStatus, pageable).map(OrderMapper::toDto);
	}

	// 修改訂單狀態
	@Transactional
	public OrderResponseDto updateStatus(Long id, String status) {
		Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));

		order.setStatus(Order.OrderStatus.valueOf(status));
		return OrderMapper.toDto(order);
	}

	// 刪除訂單
	@Transactional
	public void deleteOrder(Long id) {
		if (!orderRepository.existsById(id)) {
			throw new RuntimeException("Order not found");
		}
		orderRepository.deleteById(id);
	}

	// 月營收
	public List<Map<String, Object>> getMonthlyRevenue(int year) {
		return orderRepository.getMonthlyRevenue(year);
	}

	// 訂單趨勢
	public List<Map<String, Object>> getOrderTrend(LocalDate start, LocalDate end) {
		return orderRepository.getOrderTrend(start.atStartOfDay(), end.plusDays(1).atStartOfDay());
	}
}
