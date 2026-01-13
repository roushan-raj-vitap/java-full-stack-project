package com.freelance.server.controller;

//MessageController.java
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.freelance.server.dto.MessageRequest;
import com.freelance.server.model.Message;
import com.freelance.server.service.MessageService;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

 private final MessageService messageService;

 @PostMapping
 public Message sendMessage(@RequestParam Long senderId, @RequestBody MessageRequest request) {
     return messageService.sendMessage(senderId, request);
 }

 @GetMapping("/{userId1}/{userId2}")
 public List<Message> getConversation(@PathVariable Long userId1, @PathVariable Long userId2) {
     return messageService.getConversation(userId1, userId2);
 }
}
