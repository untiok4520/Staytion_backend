package com.example.demo.repository;

import com.example.demo.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long>{
    @Query("""
            select m from message m 
            where(m.senderId = :user1 and m.receiverId = :user2
            OR m.senderId = :user2 AND m.receiverId = :user1)
            and m.hotel = :hotelId
            order by m.sentAt
            
            """)
    List<Message>findChatHistory(@Param("user1") Long user1, @Param("user2") Long user2, @Param("hotelId") Long hotelId);
    List<Message> findByChatRoomOrderBySentAt(Long chatRoom);
}
