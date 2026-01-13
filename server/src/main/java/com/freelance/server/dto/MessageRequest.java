package com.freelance.server.dto;

import lombok.Data;

@Data
public class MessageRequest {
 private Long receiverId;
 private String content;
}
