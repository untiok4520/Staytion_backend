package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.RoomAvailabilityCheckResponse;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.RoomAvailability;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.RoomAvailabilityRepository;
import com.example.demo.repository.RoomTypeRepository;

@Service
public class RoomAvailabilityService {

    @Autowired
    private RoomAvailabilityRepository roomAvailabilityRepository;

    @Autowired
    private RoomTypeRepository roomTypeRepository;
    
    @Autowired
    private OrderItemRepository orderItemRepository;
    
    public Optional<RoomAvailability>
        findByRoomTypeAndDate(Long roomTypeId, LocalDate date) {
        return roomAvailabilityRepository.findByRoomType_IdAndDate(roomTypeId, date);
    }

    public List<RoomAvailabilityCheckResponse>
        findByRoomTypeAndDateBetween (Long roomTypeId, LocalDate start, LocalDate end) {
        List<RoomAvailability> entities = roomAvailabilityRepository.findByRoomType_IdAndDateBetween(roomTypeId, start, end);

        return entities.stream()
                .map(e -> new RoomAvailabilityCheckResponse(
                        e.getId(),
                        e.getRoomType().getId(),
                        e.getDate(),
                        e.getAvailableQuantity()
                ))
                .collect(java.util.stream.Collectors.toList());
    }

    public int getAvailableCount(Long roomTypeId, LocalDate checkInDate, LocalDate checkOutDate) {
        List<RoomAvailability> availList = roomAvailabilityRepository.findByRoomType_IdAndDateBetween(
                roomTypeId, checkInDate, checkOutDate.minusDays(1)); // checkout 不含最後一天

        if (availList.isEmpty()) {
            return 0;
        }
        return availList.stream()
                .mapToInt(RoomAvailability::getAvailableQuantity)
                .min()
                .orElse(0);
    }
    
    // 更新訂單
    public int getAvailableCount(Long roomTypeId, LocalDate checkIn, LocalDate checkOut, Long excludeOrderId) {
        int totalRooms = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new RuntimeException("找不到房型"))
                .getQuantity(); // 假設你有這個欄位表示總房間數

        // 查找所有其他訂單中已訂的數量（不包含此訂單）
        List<OrderItem> overlappingItems = orderItemRepository
                .findOverlappingItems(roomTypeId, checkIn, checkOut, excludeOrderId);

        int booked = overlappingItems.stream()
                .mapToInt(OrderItem::getQuantity)
                .sum();

        return totalRooms - booked;
    }

}
