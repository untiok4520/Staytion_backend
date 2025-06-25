package com.example.demo.service;

import com.example.demo.dto.BookingRequest;
import com.example.demo.dto.BookingResponse;
import com.example.demo.dto.OrderItemRequest;
import com.example.demo.dto.OrderItemResponse;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoomTypeRepository roomTypeRepository;
	@Autowired
	private HotelRepository hotelRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private OrderItemRepository orderItemRepository;
	@Autowired
	private RoomAvailabilityService roomAvailabilityService;

	@Transactional
	public BookingResponse createBooking(BookingRequest dto) {
		User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));

		LocalDate checkIn = LocalDate.parse(dto.getCheckInDate());
		LocalDate checkOut = LocalDate.parse(dto.getCheckOutDate());

		for (OrderItemRequest itemDto : dto.getItems()) {
			Long roomTypeId = itemDto.getRoomTypeId();
			int quantity = itemDto.getQuantity();
			int available = roomAvailabilityService.getAvailableCount(roomTypeId, checkIn, checkOut);
			if (quantity > available) {
				throw new RuntimeException("房型 " + roomTypeId + " 預訂數量超過可訂房數，剩餘 " + available + " 間。");
			}
		}

		Order order = new Order();
		order.setUser(user);
		order.setCheckInDate(LocalDate.parse(dto.getCheckInDate()));
		order.setCheckOutDate(LocalDate.parse(dto.getCheckOutDate()));
		order.setStatus(Order.OrderStatus.CONFIRMED);
		order.setCreatedAt(LocalDateTime.now());

		BigDecimal total = BigDecimal.ZERO;
		List<OrderItem> orderItems = new java.util.ArrayList<>();
		List<OrderItemResponse> itemResponses = new java.util.ArrayList<>();

		for (OrderItemRequest itemDto : dto.getItems()) {
			RoomType roomType = roomTypeRepository.findById(itemDto.getRoomTypeId())
					.orElseThrow(() -> new RuntimeException("RoomType not found: " + itemDto.getRoomTypeId()));
			Hotel hotel = roomType.getHotel();
			BigDecimal subtotal = roomType.getPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity()));
			total = total.add(subtotal);

			OrderItem orderItem = new OrderItem();
			orderItem.setOrder(order);
			orderItem.setRoomType(roomType);
			orderItem.setQuantity(itemDto.getQuantity());
			orderItem.setPricePerRoom(roomType.getPrice());
			orderItem.setSubtotal(subtotal);
			orderItems.add(orderItem);

			OrderItemResponse ir = new OrderItemResponse();
			ir.setRoomTypeId(roomType.getId());
			ir.setRoomTypeName(roomType.getRname());
			ir.setRoomImgUrl(roomType.getImgUrl());
			ir.setHotelId(hotel.getId());
			ir.setHotelName(hotel.getHname());
			ir.setQuantity(itemDto.getQuantity());
			ir.setPricePerRoom(roomType.getPrice());
			ir.setSubtotal(subtotal);
			itemResponses.add(ir);
		}

		order.setTotalPrice(total);
		order.setOrderItems(orderItems);
		orderRepository.save(order); // 會 cascade 一併存 orderItems

		BookingResponse response = new BookingResponse();
		response.setOrderId(order.getId());
		response.setStatus(order.getStatus().name());
		response.setCheckInDate(order.getCheckInDate().toString());
		response.setCheckOutDate(order.getCheckOutDate().toString());
		response.setTotalPrice(order.getTotalPrice());
		response.setItems(itemResponses);
		response.setMessage("預訂成功");

		return response;
	}

	public List<BookingResponse> getBookingsByUser(Long userId) {
		List<Order> orders = orderRepository.findByUserId(userId);
		return orders.stream().map(this::toResponseDto).collect(Collectors.toList());
	}

	public BookingResponse getBookingById(Long bookingId) {
		Order order = orderRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("找不到此訂單"));
		return toResponseDto(order);
	}

	@Transactional
	public BookingResponse cancelBooking(Long bookingId) {
		Order order = orderRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("找不到此訂單"));
		order.setStatus(Order.OrderStatus.CANCELED);
		orderRepository.save(order);
		return toResponseDto(order);
	}

	// 更新訂單 (新增的)
	@Transactional
	public BookingResponse updateBooking(Long orderId, BookingRequest dto) {
		// 1. 找訂單
		Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("找不到訂單"));
		// 2. 轉換日期
		LocalDate newCheckIn = LocalDate.parse(dto.getCheckInDate());
		LocalDate newCheckOut = LocalDate.parse(dto.getCheckOutDate());
		// 3. 檢查每個房型剩餘數量
		for (OrderItemRequest item : dto.getItems()) {
			Long roomTypeId = item.getRoomTypeId();
			int quantity = item.getQuantity();
			int available = roomAvailabilityService.getAvailableCount(roomTypeId, newCheckIn, newCheckOut, orderId);
			if (quantity > available) {
				throw new RuntimeException("房型 " + roomTypeId + " 剩餘 " + available + " 間，無法更新。");
			}
		}
		// 4. 清空舊的 OrderItems
		order.getOrderItems().clear();
		// 5. 更新訂單基本資料，並加到 OrderItems 裡
		order.setCheckInDate(newCheckIn);
		order.setCheckOutDate(newCheckOut);
		BigDecimal total = BigDecimal.ZERO;
		for (OrderItemRequest itemDto : dto.getItems()) {
			RoomType roomType = roomTypeRepository.findById(itemDto.getRoomTypeId())
					.orElseThrow(() -> new RuntimeException("找不到房型：" + itemDto.getRoomTypeId()));

			BigDecimal subtotal = roomType.getPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity()));
			total = total.add(subtotal);

			OrderItem newItem = new OrderItem();
			newItem.setOrder(order);
			newItem.setRoomType(roomType);
			newItem.setQuantity(itemDto.getQuantity());
			newItem.setPricePerRoom(roomType.getPrice());
			newItem.setSubtotal(subtotal);
			order.getOrderItems().add(newItem);
		}
		// 6. 設定新的總價
		order.setTotalPrice(total);
		// 7. 儲存
		orderRepository.save(order); // cascade OrderItems
		// 8. 回傳更新後的資料
		return toResponseDto(order);
	}

	// toDTO
	private BookingResponse toResponseDto(Order order) {
		BookingResponse dto = new BookingResponse();
		dto.setOrderId(order.getId());
		dto.setStatus(order.getStatus().name());
		dto.setCheckInDate(order.getCheckInDate().toString());
		dto.setCheckOutDate(order.getCheckOutDate().toString());
		dto.setTotalPrice(order.getTotalPrice());
		dto.setMessage(null);

		if (order.getOrderItems() != null) {
			List<OrderItemResponse> itemResponses = order.getOrderItems().stream().map(item -> {
				OrderItemResponse itemDto = new OrderItemResponse();
				itemDto.setRoomTypeId(item.getRoomType().getId());
				itemDto.setRoomTypeName(item.getRoomType().getRname());
				itemDto.setQuantity(item.getQuantity());
				itemDto.setPricePerRoom(item.getPricePerRoom());
				itemDto.setSubtotal(item.getSubtotal());
				return itemDto;
			}).collect(Collectors.toList());
			dto.setItems(itemResponses);
		}
		return dto;
	}
}