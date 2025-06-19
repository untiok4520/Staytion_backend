package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.OrderRequestDto;
import com.example.demo.dto.response.OrderResponseDto;
import com.example.demo.entity.Order;
import com.example.demo.service.AdminOrderService;

@RestController
@RequestMapping("/api/admins/orders")
public class AdminOrderController {
	@Autowired
	private AdminOrderService service;

	@GetMapping
	public List<OrderResponseDto> getAll() {
		return service.getAllOrders();
	}

	@PutMapping("/{id}/status")
	public OrderResponseDto updateStatus(@PathVariable Long id, @RequestParam String status) {
		return service.updateStatus(id, status);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		service.deleteOrder(id);
	}

	// 狀態 + 日期 + 關鍵字篩選 + 分頁查詢（後台篩選）
	@GetMapping("/filter")
	public Page<OrderResponseDto> filterOrders(@RequestParam(required = false) Order.OrderStatus status,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
			@RequestParam(required = false) String keyword, @RequestParam(required = false) Long currentUserId, // 暫時用來模擬登入
			Pageable pageable) {
		return service.searchOrders(currentUserId, status, start, end, keyword, pageable);
	}
}
