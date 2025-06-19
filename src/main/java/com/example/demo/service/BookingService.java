package com.example.demo.service;

import com.example.demo.dto.BookingRequest;
import com.example.demo.dto.BookingResponse;
import com.example.demo.entity.Hotel;
import com.example.demo.entity.RoomType;
import com.example.demo.repository.HotelRepository;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.RoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    @Autowired
    private RoomTypeRepository roomTypeRepository;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    public BookingResponse createBooking(BookingRequest req, Integer userId) {
        RoomType roomType = roomTypeRepository.findById(req.getRoomTypeId())
                .orElseThrow(() -> new RuntimeException("房型不存在"));
        Hotel hotel = roomType.getHotel();
                if (hotel == null) {
                    throw new RuntimeException("飯店不存在");
                }

        return null;
    }
}
