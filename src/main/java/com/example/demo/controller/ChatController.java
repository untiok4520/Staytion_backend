package com.example.demo.controller;

import com.example.demo.entity.ChatRoom;
import com.example.demo.entity.Hotel;
import com.example.demo.entity.Message;
import com.example.demo.entity.User;
import com.example.demo.repository.ChatRoomRepository;
import com.example.demo.repository.MessageRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ChatController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @MessageMapping("/send")
    public void sendMessage(Message message) {
        System.out.println("收到訊息：" + message.getContent());
        message.setSentAt(LocalDateTime.now());
        messageRepository.save(message);
        //廣播出去
        messagingTemplate.convertAndSend("/topic/chat/" + message.getHotel().getId(), message);
    }
    @GetMapping("/user/{userId}")
    @Operation(summary = "某使用者的所有聊天室")
    public List<ChatRoom> getChatRoomsByUser(@PathVariable Long userId) {
        User user = new User();
        user.setId(userId);
        return chatRoomRepository.findByUser(user);
    }
    @GetMapping("/hotel/{hotelId}")
    @Operation(summary = "某飯店的所有聊天室")
    public List<ChatRoom> getChatRoomsByHotel(@PathVariable Long hotelId) {
        Hotel hotel = new Hotel();
        hotel.setId(hotelId);
        return chatRoomRepository.findByHotel(hotel);
    }

}
