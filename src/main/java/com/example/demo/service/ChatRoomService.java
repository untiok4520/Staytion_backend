package com.example.demo.service;

import com.example.demo.dto.ChatRoomDto;
import com.example.demo.dto.MessageDto;
import com.example.demo.entity.ChatRoom;
import com.example.demo.entity.Message;
import com.example.demo.repository.ChatRoomRepository;
import com.example.demo.repository.HotelRepository;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatRoomService {
    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private MessageRepository messageRepository;

    // 新增：根據兩個用戶和飯店查找或創建聊天室

    /**
     * senderId 對應 user1，receiverId 對應 user2
     * 若反過來查不到，會嘗試以 receiverId 作為 user1 查一次
     */
    public ChatRoom findOrCreateChatRoom(Long user1Id, Long user2Id, Long hotelId) {
        // 先查詢 user1Id-user2Id
        Optional<ChatRoom> optionalRoom = chatRoomRepository.findByUser1IdAndUser2IdAndHotel_Id(user1Id, user2Id, hotelId);
        if (optionalRoom.isEmpty()) {
            // 再查詢 user2Id-user1Id
            optionalRoom = chatRoomRepository.findByUser1IdAndUser2IdAndHotel_Id(user2Id, user1Id, hotelId);
        }
        if (optionalRoom.isPresent()) {
            return optionalRoom.get();
        }
        // 創建新聊天室
        ChatRoom newRoom = new ChatRoom();
        newRoom.setUser1(userRepository.findById(user1Id).orElseThrow());
        newRoom.setUser2(userRepository.findById(user2Id).orElseThrow());
        newRoom.setHotel(hotelRepository.findById(hotelId).orElseThrow());
        newRoom.setCreatedAt(LocalDateTime.now());
        newRoom.setUpdatedAt(LocalDateTime.now());
        return chatRoomRepository.save(newRoom);
    }

    // 新增: 查詢聊天室歷史訊息並回傳 List<MessageDto>
    public List<MessageDto> getChatHistory(Long senderId, Long receiverId, Long hotelId) {
        // 支援發話方與接收方順序不固定
        Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findByUser1IdAndUser2IdAndHotel_Id(senderId, receiverId, hotelId);
        if (chatRoomOptional.isEmpty()) {
            chatRoomOptional = chatRoomRepository.findByUser1IdAndUser2IdAndHotel_Id(receiverId, senderId, hotelId);
        }
        if (chatRoomOptional.isEmpty()) {
            return new ArrayList<>();
        }
        List<Message> messages = messageRepository.findByChatRoomIdOrderBySentAt(chatRoomOptional.get().getId());
        return messages.stream().map(this::toDto).collect(Collectors.toList());
    }

    // 將 Message 實體轉換為 MessageDto
    private MessageDto toDto(Message message) {
        MessageDto dto = new MessageDto();
        dto.setId(message.getId());
        dto.setChatRoomId(message.getChatRoom() != null ? message.getChatRoom().getId() : null);
        dto.setSenderId(message.getSender() != null ? message.getSender().getId() : null);
        dto.setReceiverId(message.getReceiver() != null ? message.getReceiver().getId() : null);
        dto.setContent(message.getContent());
        dto.setSentAt(message.getSentAt());
        return dto;
    }

    // 取得某用戶相關的聊天室列表
    public List<ChatRoomDto> getChatRoomsForUser(Long userId) {
        System.out.println("🔍 準備查詢聊天室，使用者 userId = " + userId);

        List<ChatRoom> chatRooms = chatRoomRepository.findByUser1_IdOrUser2_Id(userId, userId);
        System.out.println("📦 查到聊天室筆數: " + chatRooms.size());
        return chatRooms.stream().map(room -> {
            ChatRoomDto dto = new ChatRoomDto();
            dto.setChatRoomId(room.getId());
            dto.setUpdatedAt(room.getUpdatedAt());
            dto.setLastMessage(room.getLastMessage());
            dto.setHotelId(room.getHotel().getId());

            Long ownerId = room.getHotel().getOwner().getId();
            if (userId.equals(ownerId)) {
                Long guestId = room.getUser1().getId().equals(ownerId)
                        ? room.getUser2().getId() : room.getUser1().getId();
                dto.setReceiverId(guestId);
                dto.setDisplayName(userRepository.findById(guestId)
                        .map(u -> u.getFirstName())
                        .orElse("未知房客"));
            } else {
                Long owner = room.getHotel().getOwner().getId();
                dto.setReceiverId(owner);
                dto.setDisplayName(room.getHotel().getHname());
            }
            return dto;
        }).collect(Collectors.toList());
    }
}