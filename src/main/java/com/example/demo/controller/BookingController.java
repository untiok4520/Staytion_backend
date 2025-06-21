package com.example.demo.controller;

import com.example.demo.dto.BookingRequest;
import com.example.demo.dto.BookingResponse;
import com.example.demo.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Operation(
            summary = "建立/新增訂單",
            description = "建立一筆新訂單,需傳入會員ID、入住/退房日期、訂購房型與數量等。回傳訂單ID、狀態、入住/退房日期、總金額、各房型明細"
    )
    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@RequestBody BookingRequest dto) {
        BookingResponse response = bookingService.createBooking(dto);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "查詢個人所有訂單",
            description = "依會員ID查詢該會員所有訂單,每筆資料包含:訂單ID、狀態、入住/退房日期、總金額、各房型明細"
    )
    @GetMapping("/me")
    public ResponseEntity<List<BookingResponse>> getMyBookings(@RequestParam Long userId) {
        List<BookingResponse> orders = bookingService.getBookingsByUser(userId);
        return ResponseEntity.ok(orders);
    }

    @Operation(
            summary = "查詢單筆訂單",
            description = "依訂單ID查詢單筆訂單詳細資訊,內容包含:訂單ID、狀態、入住/退房日期、總金額、各房型明細(房型ID、名稱、數量、單價、小計)"
    )
    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingResponse> getBooking(@PathVariable Long bookingId) {
        BookingResponse order = bookingService.getBookingById(bookingId);
        return ResponseEntity.ok(order);
    }

    @Operation(
            summary = "取消訂單",
            description = "依訂單ID取消該筆訂單,回傳已取消訂單的詳細資訊,內容包含:訂單ID、狀態、入住/退房日期、金額、各房型明細"
    )
    @PutMapping("/{bookingId}/cancel")
    public ResponseEntity<BookingResponse> cancelBooking(@PathVariable Long bookingId) {
        BookingResponse result = bookingService.cancelBooking(bookingId);
        return ResponseEntity.ok(result);
    }
}
