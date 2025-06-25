package com.example.demo.controller;

import com.example.demo.dto.MessageDto;
import com.example.demo.entity.ChatRoom;
import com.example.demo.entity.Hotel;
import com.example.demo.entity.Message;
import com.example.demo.entity.User;
import com.example.demo.repository.ChatRoomRepository;
import com.example.demo.repository.HotelRepository;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ChatRoomService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
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
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ChatRoomService chatRoomService;

    @MessageMapping("/send")
    @Transactional
    public void sendMessage(MessageDto dto) {
        try {
            System.out.println("收到 WebSocket 訊息: " + dto);
            // Validate input IDs
            if (dto.getSenderId() == null || dto.getReceiverId() == null || dto.getHotelId() == null) {
                System.out.println("DTO 資料不齊全");
                // Optionally log or throw a runtime exception
                return;
            }
            User sender = userRepository.findById(dto.getSenderId()).orElse(null);
            User receiver = userRepository.findById(dto.getReceiverId()).orElse(null);
            Hotel hotel = hotelRepository.findById(dto.getHotelId()).orElse(null);
            if (sender == null || receiver == null || hotel == null) {
                System.out.println("查不到 sender/receiver/hotel");
                // Optionally log or throw a runtime exception
                return;
            }
            ChatRoom chatRoom = chatRoomService.findOrCreateChatRoom(sender.getId(), receiver.getId(), hotel.getId());
            Message message = new Message();
            message.setSender(sender);
            message.setReceiver(receiver);
            message.setHotel(hotel);
            message.setChatRoom(chatRoom);
            message.setContent(dto.getContent());
            message.setSentAt(LocalDateTime.now());
            Message savedMessage = messageRepository.save(message);
            System.out.println("儲存訊息成功，ID: " + savedMessage.getId());
            chatRoom.setLastMessage(savedMessage.getContent());
            chatRoom.setUpdatedAt(LocalDateTime.now());
            chatRoomRepository.save(chatRoom);

            MessageDto outDto = new MessageDto();
            outDto.setId(savedMessage.getId());
            outDto.setSenderId(savedMessage.getSender() != null ? savedMessage.getSender().getId() : null);
            outDto.setReceiverId(savedMessage.getReceiver() != null ? savedMessage.getReceiver().getId() : null);
            outDto.setHotelId(savedMessage.getHotel() != null ? savedMessage.getHotel().getId() : null);
            outDto.setChatRoomId(savedMessage.getChatRoom() != null ? savedMessage.getChatRoom().getId() : null);
            outDto.setContent(savedMessage.getContent());
            outDto.setSentAt(savedMessage.getSentAt());
            messagingTemplate.convertAndSend("/topic/chat/" + chatRoom.getId(), outDto);
        } catch (Exception e) {
            System.err.println("處理訊息時發生錯誤：" + e.getMessage());
            e.printStackTrace();
        }

    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "某使用者的所有聊天室")
    public List<ChatRoom> getChatRoomsByUser(@PathVariable Long userId) {
        User user = new User();
        user.setId(userId);
        return chatRoomRepository.findByUser1OrUser2(user, user);
    }

    @GetMapping("/hotel/{hotelId}")
    @Operation(summary = "某飯店的所有聊天室")
    public List<ChatRoom> getChatRoomsByHotel(@PathVariable Long hotelId) {
        return chatRoomRepository.findByHotel_Id(hotelId);
    }
}
