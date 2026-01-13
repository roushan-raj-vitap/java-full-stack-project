package com.freelance.server.dto;

import lombok.Data;

@Data
public class ChatMessage {
 private String senderId;
 private String receiverId;
 private String content;
 private String timestamp;
}
