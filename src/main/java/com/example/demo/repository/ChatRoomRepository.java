package com.example.demo.repository;

import com.example.demo.entity.ChatRoom;
import com.example.demo.entity.Hotel;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findByHotel(Hotel hotel);
    List<ChatRoom> findByUser(User user);
    Optional<ChatRoom> findByUserAndHotel(User user, Hotel hotel);
}
