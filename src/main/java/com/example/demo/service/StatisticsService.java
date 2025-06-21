//package com.example.demo.service;
//
//import com.example.demo.repository.OrderRepository;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class StatisticsService {
//
//    private final OrderRepository orderRepository;
//
//    public StatisticsService(OrderRepository orderRepository) {
//        this.orderRepository = orderRepository;
//    }
//
//    public List<Map<String, Object>> getDailyRevenue(int year, int month) {
//        return orderRepository.getDailyRevenue(year, month);
//    }
//
//    public List<Map<String, Object>> getDailyADR(int year, int month) {
//        return orderRepository.getDailyADR(year, month);
//    }
//
//    public List<Map<String, Object>> getDailyOccupancy(int year, int month) {
//        return orderRepository.getDailyOccupancy(year, month);
//    }
//
//    public List<Map<String, Object>> getRoomTypeRevenue(int year) {
//        return orderRepository.getRoomTypeRevenue(year);
//    }
//
//    public BigDecimal getAnnualRevenue(int year) {
//        return orderRepository.getAnnualRevenue(year);
//    }
//
//    public long getValidOrderCount() {
//        return orderRepository.countValidOrders();
//    }
//}
//
