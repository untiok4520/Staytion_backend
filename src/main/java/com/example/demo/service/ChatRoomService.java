package com.example.demo.service;

import com.example.demo.entity.ChatRoom;
import com.example.demo.entity.Hotel;
import com.example.demo.entity.User;
import com.example.demo.repository.ChatRoomRepository;
import com.example.demo.repository.HotelRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ChatRoomService {
    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HotelRepository hotelRepository;

    public ChatRoom findOrCreate(Long userId, Long hotelId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel not found with ID: " + hotelId));

        return chatRoomRepository.findByUserAndHotel(user, hotel)
                .orElseGet(() -> {
                    ChatRoom newRoom = new ChatRoom();
                    newRoom.setUser(user);
                    newRoom.setHotel(hotel);
                    newRoom.setCreatedAt(LocalDateTime.now());
                    newRoom.setUpdatedAt(LocalDateTime.now());
                    return chatRoomRepository.save(newRoom);
                });
    }
}
