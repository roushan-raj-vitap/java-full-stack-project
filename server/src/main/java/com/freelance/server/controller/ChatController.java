package com.freelance.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.freelance.server.dto.ChatMessage;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class ChatController {

 private final SimpMessagingTemplate messagingTemplate;

 @MessageMapping("/sendMessage") // /app/sendMessage
 public void sendMessage(ChatMessage message) {
     message.setTimestamp(LocalDateTime.now().toString());
     // Send to the receiver's subscription
     messagingTemplate.convertAndSend("/topic/messages/" + message.getReceiverId(), message);
 }
}
