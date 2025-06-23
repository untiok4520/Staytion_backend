package com.example.demo.controller;

import com.example.demo.entity.ChatRoom;
import com.example.demo.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/chatrooms")
public class ChatRoomController {

    @Autowired
    private ChatRoomService chatRoomService;

    @PostMapping("/find-or-create")
    public Map<String, Long> findOrCreateChatRoom(@RequestBody Map<String, Long> request) {
        Long senderId = request.get("senderId");
        Long receiverId = request.get("receiverId");
        Long hotelId = request.get("hotelId");

        ChatRoom chatRoom = chatRoomService.findOrCreateChatRoom(senderId, receiverId, hotelId);

        Map<String, Long> response = new HashMap<>();
        response.put("chatRoomId", chatRoom.getId());
        return response;
    }
}
