package com.example.demo.controller;

import com.example.demo.dto.ChatRoomDto;
import com.example.demo.dto.request.ChatRoomRequestDto;
import com.example.demo.entity.ChatRoom;
import com.example.demo.repository.ChatRoomRepository;
import com.example.demo.service.ChatRoomService;
import com.example.demo.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/chatrooms")
public class ChatRoomController {

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private JwtService jwtService;
    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @PostMapping("/find-or-create")
    public Map<String, Long> findOrCreateChatRoom(
            @RequestBody ChatRoomRequestDto requestDto,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        Long senderId = jwtService.getUserIdFromToken(token); // 從 JWT 解析 userId
        Long receiverId = requestDto.getReceiverId();
        Long hotelId = requestDto.getHotelId();

        ChatRoom chatRoom = chatRoomService.findOrCreateChatRoom(senderId, receiverId, hotelId);
        chatRoom = chatRoomRepository.save(chatRoom);
        System.out.println("建立聊天室成功 ID: " + chatRoom.getId());
        Map<String, Long> response = new HashMap<>();
        response.put("chatRoomId", chatRoom.getId());
        System.out.println("即將回傳 JSON: " + response);
        return response;

    }

    @GetMapping("/my")
    public List<ChatRoomDto> getMyChatRooms(@RequestHeader("Authorization") String authHeader) {
        System.out.println("✅ getMyChatRooms 執行了！");
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtService.getUserIdFromToken(token);

        return chatRoomService.getChatRoomsForUser(userId);
    }
}
