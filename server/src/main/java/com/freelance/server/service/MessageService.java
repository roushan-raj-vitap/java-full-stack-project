package com.freelance.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.freelance.server.dto.MessageRequest;
import com.freelance.server.model.Message;
import com.freelance.server.model.User;
import com.freelance.server.repository.MessageRepository;
import com.freelance.server.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

 private final MessageRepository messageRepository;
 private final UserRepository userRepository;

 public Message sendMessage(Long senderId, MessageRequest request) {
     User sender = userRepository.findById(senderId)
             .orElseThrow(() -> new RuntimeException("Sender not found"));
     User receiver = userRepository.findById(request.getReceiverId())
             .orElseThrow(() -> new RuntimeException("Receiver not found"));

     Message message = Message.builder()
             .sender(sender)
             .receiver(receiver)
             .content(request.getContent())
             .timestamp(LocalDateTime.now())
             .build();

     return messageRepository.save(message);
 }

 public List<Message> getConversation(Long userId1, Long userId2) {
     return messageRepository.findBySenderIdAndReceiverIdOrSenderIdAndReceiverIdOrderByTimestampAsc(
             userId1, userId2, userId2, userId1
     );
 }
}
