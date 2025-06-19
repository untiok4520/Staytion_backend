package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.OrderRequestDto;
import com.example.demo.dto.response.OrderResponseDto;
import com.example.demo.service.UserOrderService;

@RestController
@RequestMapping("/api/users/orders")
public class UserOrderController {
	@Autowired
	private UserOrderService service;

	@PostMapping
	public OrderResponseDto create(@RequestBody OrderRequestDto dto) {
		return service.createOrder(dto);
	}

//	@GetMapping("/user/{userId}")
//	public List<OrderResponseDto> getUserOrders(@PathVariable Long userId) {
//		return service.getOrdersByUserId(userId);
//	}
	@GetMapping("/user/{userId}/orders")
	public Page<OrderResponseDto> getUserOrders(@PathVariable Long userId, Pageable pageable) {
		return service.getOrdersByUserId(userId, pageable);
	}

	@PutMapping("/{id}")
	public OrderResponseDto updateOrder(@PathVariable Long id, @RequestBody OrderRequestDto dto) {
		return service.updateOrder(id, dto);
	}
}
