package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
}
