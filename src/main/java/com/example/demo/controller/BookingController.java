package com.example.demo.controller;

import com.example.demo.dto.BookingRequest;
import com.example.demo.dto.BookingResponse;
import com.example.demo.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    //建立訂單
    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@RequestBody BookingRequest dto) {
        BookingResponse response = bookingService.createBooking(dto);
        return ResponseEntity.ok(response);
    }

    //個人所有訂單
    @GetMapping("/me")
    public ResponseEntity<List<BookingResponse>> getMyBookings(@RequestParam Long userId) {
        List<BookingResponse> orders = bookingService.getBookingsByUser(userId);
        return ResponseEntity.ok(orders);
    }

    //查詢單一訂單
    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingResponse> getBooking(@PathVariable Long bookingId) {
        BookingResponse order = bookingService.getBookingById(bookingId);
        return ResponseEntity.ok(order);
    }

    //取消訂單
    @PutMapping("/{bookingId}/cancel")
    public ResponseEntity<BookingResponse> cancelBooking(@PathVariable Long bookingId) {
        BookingResponse result = bookingService.cancelBooking(bookingId);
        return ResponseEntity.ok(result);
    }
}
