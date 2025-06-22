package com.example.demo.controller;

import com.example.demo.dto.MessageDto;
import com.example.demo.entity.ChatRoom;
import com.example.demo.entity.Message;
import com.example.demo.repository.ChatRoomRepository;
import com.example.demo.repository.HotelRepository;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @GetMapping
    public List<Message> getChatHistory(@RequestParam Long senderId,
                                        @RequestParam Long receiverId,
                                        @RequestParam Long hotelId
    ) {
        return messageRepository.findChatHistory(senderId, receiverId, hotelId);
    }

    @PostMapping
    public Message sendMessage(@RequestBody MessageDto dto) {
        ChatRoom chatRoom = chatRoomService.findOrCreateChatRoom(dto.getSenderId(), dto.getReceiverId(), dto.getHotelId());

        Message message = new Message();
        message.setSender(userRepository.findById(dto.getSenderId()).orElseThrow());
        message.setReceiver(userRepository.findById(dto.getReceiverId()).orElseThrow());
        message.setChatRoom(chatRoom);
        message.setContent(dto.getContent());
        message.setSentAt(LocalDateTime.now());
        message.setIsRead(false);

        chatRoom.setLastMessage(dto.getContent());
        chatRoom.setUpdatedAt(LocalDateTime.now());

        chatRoomRepository.save(chatRoom);
        return messageRepository.save(message);
    }

    @GetMapping("/{chatRoomId}/messages")
    public List<MessageDto> getMessages(@PathVariable Long chatRoomId, @RequestParam Long userId) {
        List<Message> messages = messageRepository.findByChatRoomIdOrderBySentAt(chatRoomId);

        // 標記為已讀
        messages.stream()
                .filter(m -> !m.getSender().getId().equals(userId) && !m.getIsRead())
                .forEach(m -> {
                    m.setIsRead(true);
                    messageRepository.save(m);
                });

        return messages.stream().map(m -> {
            MessageDto dto = new MessageDto();
            dto.setId(m.getId());
            dto.setSenderId(m.getSender().getId());
            dto.setSenderName(m.getSender().getFirstName());
            dto.setReceiverId(m.getReceiver().getId());
            dto.setHotelId(m.getHotel().getId());
            dto.setChatRoomId(m.getChatRoom().getId());
            dto.setContent(m.getContent());
            dto.setSentAt(m.getSentAt());
            return dto;
        }).toList();
    }
}
